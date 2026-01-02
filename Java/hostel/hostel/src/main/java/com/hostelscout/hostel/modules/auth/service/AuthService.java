package com.hostelscout.hostel.modules.auth.service;

import com.hostelscout.hostel.modules.admin.entity.Admin;
import com.hostelscout.hostel.modules.admin.mapper.AdminMapper;
import com.hostelscout.hostel.modules.admin.repository.AdminRepository;
import com.hostelscout.hostel.modules.auth.dto.LoginResponseDto;
import com.hostelscout.hostel.modules.common.entity.BaseUser;
import com.hostelscout.hostel.modules.common.enums.Role;
import com.hostelscout.hostel.modules.common.repository.BaseUserRepository;
import com.hostelscout.hostel.modules.common.security.jwt.JwtService;
import com.hostelscout.hostel.modules.hostelowner.entity.HostelOwner;
import com.hostelscout.hostel.modules.hostelowner.mapper.HostelOwnerMapper;
import com.hostelscout.hostel.modules.hostelowner.repository.HostelOwnerRepository;
import com.hostelscout.hostel.modules.user.entity.User;
import com.hostelscout.hostel.modules.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    private final BaseUserRepository baseUserRepository;
    private final AdminRepository adminRepository;
    private final HostelOwnerRepository hostelOwnerRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AdminMapper adminMapper;
    private final HostelOwnerMapper hostelOwnerMapper;

    /**
     * Unified login endpoint for all roles (ADMIN, HOSTEL_OWNER, HOSTEL_RESIDENT)
     *
     * @param username The username
     * @param rawPassword The plain password
     * @return LoginResponseDto with JWT token, role, and user details
     */
    @Transactional(readOnly = true)
    public LoginResponseDto authenticate(String username, String rawPassword) {
        // Step 1: Find BaseUser by username
        Optional<BaseUser> baseUserOpt = baseUserRepository.findByUsername(username);
        BaseUser baseUser = baseUserOpt.orElseThrow(
                () -> new EntityNotFoundException("Invalid username or password"));

        // Step 2: Validate password
        if (!passwordEncoder.matches(rawPassword, baseUser.getPassword())) {
            throw new EntityNotFoundException("Invalid username or password");
        }

        // Step 3: Get the role from BaseUser
        Role role = baseUser.getRole();
        logger.info("User {} authenticated with role {}", username, role);

        // Step 4: Generate JWT token
        String token = jwtService.generateToken(baseUser.getUsername(), role);
        logger.info("Token generated successfully for user: {}", username);

        // Step 5: Build response based on role
        Object userDetails = getUserDetailsByRole(baseUser, role);

        return LoginResponseDto.builder()
                .token(token)
                .role(role)
                .username(baseUser.getUsername())
                .userDetails(userDetails)
                .build();
    }

    /**
     * Fetch user-specific details based on role
     */
    private Object getUserDetailsByRole(BaseUser baseUser, Role role) {
        switch (role) {
            case ADMIN:
                Admin admin = adminRepository.findByBaseUser(baseUser)
                        .orElseThrow(() -> new EntityNotFoundException("Admin record not found"));
                return adminMapper.toAdminResponseDto(admin);

            case HOSTEL_OWNER:
                HostelOwner hostelOwner = hostelOwnerRepository.findByBaseUser(baseUser)
                        .orElseThrow(() -> new EntityNotFoundException("HostelOwner record not found"));
                return hostelOwnerMapper.toResponseDto(hostelOwner);

            case HOSTEL_RESIDENT:
                User user = userRepository.findByBaseUser(baseUser)
                        .orElseThrow(() -> new EntityNotFoundException("User record not found"));
                // Return user details (you can create a UserResponseDto mapper if needed)
                return user; // For now, returning the entity directly

            default:
                throw new EntityNotFoundException("Unknown role: " + role);
        }
    }
}

