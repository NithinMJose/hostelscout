package com.hostelscout.hostel.modules.admin.service;


import com.hostelscout.hostel.modules.admin.dto.AdminCreationDto;
import com.hostelscout.hostel.modules.admin.dto.AdminresponseDto;
import com.hostelscout.hostel.modules.admin.entity.Admin;
import com.hostelscout.hostel.modules.admin.repository.AdminRepository;
import com.hostelscout.hostel.modules.common.entity.BaseUser;
import com.hostelscout.hostel.modules.common.enums.Role;
import com.hostelscout.hostel.modules.common.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BaseUserRepository baseUserRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public AdminresponseDto createAdmin(AdminCreationDto adminCreationDto) {
        try {
            //Early return if email already exists
            if (baseUserRepository.existsByEmail(adminCreationDto.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }

            // Create BaseUser
            BaseUser baseUser = BaseUser.builder()
                    .username(adminCreationDto.getUsername())
                    .email(adminCreationDto.getEmail())
                    .password(passwordEncoder.encode(adminCreationDto.getPassword()))
                    .role(Role.ADMIN)
                    .build();

            baseUser = baseUserRepository.save(baseUser);

            // Create Admin
            Admin admin = Admin.builder()
                    .baseUser(baseUser)
                    .build();
            admin = adminRepository.save(admin);

            return AdminresponseDto.builder()
                    .admin_id(admin.getAdmin_id())
                    .username(baseUser.getUsername())
                    .email(baseUser.getEmail())
                    .role(baseUser.getRole())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error creating admin: " + e.getMessage(), e);
        }
    }
}
