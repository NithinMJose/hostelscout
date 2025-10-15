package com.hostelscout.hostel.modules.admin.controller;

import com.hostelscout.hostel.modules.admin.dto.AdminCreationDto;
import com.hostelscout.hostel.modules.admin.dto.AdminResponseDto;
import com.hostelscout.hostel.modules.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminCreationDto adminCreationDto) {
            AdminResponseDto createdAdmin = adminService.createAdmin(adminCreationDto);
            return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable UUID id){
        return new ResponseEntity<>(adminService.getAdminById(id), HttpStatus.OK);
    }

    //ONLY FOR SU
    @GetMapping
    public ResponseEntity<List<?>> getAllAdmins(){
        List<AdminResponseDto> admins = adminService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }


}
