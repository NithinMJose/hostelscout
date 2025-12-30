package com.hostelscout.hostel.modules.admin.service;


import com.hostelscout.hostel.common.exception.ResourceConflictException;
import com.hostelscout.hostel.common.exception.ResourceNotFoundException;
import com.hostelscout.hostel.modules.admin.dto.AdminCreationDto;
import com.hostelscout.hostel.modules.admin.dto.AdminLoginResponseDto;
import com.hostelscout.hostel.modules.admin.dto.AdminResponseDto;
import com.hostelscout.hostel.modules.admin.dto.AdminUpdationDto;
import com.hostelscout.hostel.modules.admin.entity.Admin;
import com.hostelscout.hostel.modules.admin.enums.AdminStatus;
import com.hostelscout.hostel.modules.admin.mapper.AdminMapper;
import com.hostelscout.hostel.modules.admin.repository.AdminRepository;
import com.hostelscout.hostel.modules.common.entity.BaseUser;
import com.hostelscout.hostel.modules.common.enums.Role;
import com.hostelscout.hostel.modules.common.repository.BaseUserRepository;
import com.hostelscout.hostel.modules.common.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final Logger logger = LogManager.getLogger(AdminService.class);
    private final BaseUserRepository baseUserRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;
    private final JwtService jwtService;

    // CREATE_ADMIN SERVICE
    @Transactional
    public AdminResponseDto createAdmin(AdminCreationDto adminCreationDto) {

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
                    .adminStatus(AdminStatus.ACTIVE)
                    .statusChangedAt(LocalDateTime.now())
                    .build();
            admin = adminRepository.save(admin);

            // Prepare and return response DTO
            return adminMapper.toAdminResponseDto(admin);
    }

    //GET_ADMIN SERVICE
    @Transactional(readOnly = true)
    public AdminResponseDto getAdminById(UUID id){
        Admin admin= adminRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("Admin not found with id : "+ id));
        return adminMapper.toAdminResponseDto(admin);
    }

    //LIST_ADMINS SERVICE (Only for SU)
    @Transactional(readOnly = true)
    public List<AdminResponseDto> getAllAdmins(){
        return adminRepository.findAll()
                .stream()
                .map(adminMapper::toAdminResponseDto)
                .toList();
    }

    //UPDATE_ADMIN SERVICE
    @Transactional
    public AdminResponseDto updateAdmin(AdminUpdationDto adminUpdationDto) {
        logger.info("Inside AdminService:updateAdmin");
        // Find Admin by ID
        Admin admin = adminRepository.findById(adminUpdationDto.getAdmin_id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Can't find the admin with id " + adminUpdationDto.getAdmin_id())
                );

        // Update email fail if provided to keep it unique!
        if (adminUpdationDto.getEmail() != null) {
            logger.info("Hello World");
            BaseUser baseUser = admin.getBaseUser();
            boolean emailExists = baseUserRepository.existsByEmail(adminUpdationDto.getEmail());
            if ( emailExists && (!Objects.equals(baseUser.getEmail(), adminUpdationDto.getEmail())) ) {
                logger.info("Email can not be changed");
                throw new ResourceConflictException("Email already in use!Email should be kept unique always!" + adminUpdationDto.getEmail());
            }
            baseUser.setEmail(adminUpdationDto.getEmail());
        }

        // Update admin status if provided
        if (adminUpdationDto.getAdminStatus() != null) {
            admin.setAdminStatus(adminUpdationDto.getAdminStatus());
            logger.info("Admin Status Changed");
            admin.setStatusChangedAt(LocalDateTime.now());
        }

        // Return the updated admin
        return adminMapper.toAdminResponseDto(admin);
    }

    // AUTHENTICATION SERVICE
    @Transactional(readOnly = true)
    public AdminLoginResponseDto authenticate(String username, String rawPassword) {
        Optional<BaseUser> baseUserOpt = baseUserRepository.findByUsername(username);
        BaseUser baseUser = baseUserOpt.orElseThrow(
                () -> new ResourceNotFoundException("Invalid username or password"));

        if (!passwordEncoder.matches(rawPassword, baseUser.getPassword())) {
            throw new ResourceNotFoundException("Invalid username or password");
        }

        if (baseUser.getRole() != Role.ADMIN) {
            throw new ResourceNotFoundException("User is not an admin");
        }

        // Find corresponding Admin entity
        Admin admin = adminRepository.findByBaseUser(baseUser)
                .orElseThrow(() -> new ResourceNotFoundException("Admin record not found for user"));

        // generate JWT with username as subject
        String token = jwtService.generateToken(baseUser.getUsername(), Role.ADMIN);
        logger.info("Token Generated Successfully : "+ token);

        AdminResponseDto adminDto = adminMapper.toAdminResponseDto(admin);
        return AdminLoginResponseDto.builder()
                .token(token)
                .admin(adminDto)
                .build();
    }

}
