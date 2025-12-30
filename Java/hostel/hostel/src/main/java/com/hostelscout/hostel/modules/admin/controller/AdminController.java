package com.hostelscout.hostel.modules.admin.controller;

import com.hostelscout.hostel.modules.admin.dto.AdminCreationDto;
import com.hostelscout.hostel.modules.admin.dto.AdminLoginRequestDto;
import com.hostelscout.hostel.modules.admin.dto.AdminLoginResponseDto;
import com.hostelscout.hostel.modules.admin.dto.AdminResponseDto;
import com.hostelscout.hostel.modules.admin.dto.AdminUpdationDto;
import com.hostelscout.hostel.modules.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private static final Logger logger = LogManager.getLogger(AdminController.class);

    @PostMapping
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminCreationDto adminCreationDto) {
            AdminResponseDto createdAdmin = adminService.createAdmin(adminCreationDto);
            return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AdminLoginRequestDto loginRequest) {
        AdminLoginResponseDto response = adminService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable UUID id){
        return new ResponseEntity<>(adminService.getAdminById(id), HttpStatus.OK);
    }

    //Used to update Status
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAdmin(@Valid @RequestBody AdminUpdationDto adminUpdationDto){
        logger.info("Inside AdminController:updateAdmin");

        AdminResponseDto adminResponseDto = adminService.updateAdmin(adminUpdationDto);
        return new ResponseEntity<>(adminResponseDto, HttpStatus.OK);
    }


    //ONLY FOR SU
    @GetMapping
    public ResponseEntity<List<?>> getAllAdmins(){
        List<AdminResponseDto> admins = adminService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }


}
