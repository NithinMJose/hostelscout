package com.hostelscout.hostel.modules.auth.controller;

import com.hostelscout.hostel.modules.auth.dto.LoginRequestDto;
import com.hostelscout.hostel.modules.auth.dto.LoginResponseDto;
import com.hostelscout.hostel.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = LogManager.getLogger(AuthController.class);

    /**
     * Unified login endpoint for all user types (Admin, HostelOwner, HostelResident)
     *
     * @param loginRequest Contains username and password
     * @return LoginResponseDto with JWT token, role, and user details
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());

        LoginResponseDto response = authService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        logger.info("User {} logged in successfully with role: {}",
                loginRequest.getUsername(), response.getRole());

        return ResponseEntity.ok(response);
    }
}

