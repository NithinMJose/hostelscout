package com.hostelscout.hostel.modules.admin.service;


import com.hostelscout.hostel.common.exception.ResourceConflictException;
import com.hostelscout.hostel.modules.admin.dto.AdminCreationDto;
import com.hostelscout.hostel.modules.admin.dto.AdminresponseDto;
import com.hostelscout.hostel.modules.admin.entity.Admin;
import com.hostelscout.hostel.modules.admin.mapper.AdminMapper;
import com.hostelscout.hostel.modules.admin.repository.AdminRepository;
import com.hostelscout.hostel.modules.common.entity.BaseUser;
import com.hostelscout.hostel.modules.common.enums.Role;
import com.hostelscout.hostel.modules.common.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BaseUserRepository baseUserRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;

    @Transactional
    public AdminresponseDto createAdmin(AdminCreationDto adminCreationDto) {

            //Early return if email already exists
            if (baseUserRepository.existsByEmail(adminCreationDto.getEmail())) {
                throw new ResourceConflictException("Email already in use");
            }
            //Early return if username already exists
            if (baseUserRepository.existsByUsername(adminCreationDto.getUsername())) {
                throw new ResourceConflictException("Username already in use");
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

            // Prepare and return response DTO
            return adminMapper.toAdminesponseDto(admin);
    }
}
