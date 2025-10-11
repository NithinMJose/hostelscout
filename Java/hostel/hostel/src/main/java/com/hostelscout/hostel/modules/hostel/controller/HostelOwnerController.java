package com.hostelscout.hostel.modules.hostel.controller;

import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerCreationDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerResponseDto;
import com.hostelscout.hostel.modules.hostel.service.HostelOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/hostel-owners")
@RequiredArgsConstructor
public class HostelOwnerController {

    private final HostelOwnerService hostelOwnerService;

    @PostMapping
    public ResponseEntity<?> createHostelOwner(@Valid @RequestBody HostelOwnerCreationDto creationDto) {
        try {
            HostelOwnerResponseDto responseDto = hostelOwnerService.createHostelOwner(creationDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
