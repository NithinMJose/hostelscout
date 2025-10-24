package com.hostelscout.hostel.modules.hostel.controller;

import com.hostelscout.hostel.modules.hostel.dto.HostelCreationDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelResponseDto;
import com.hostelscout.hostel.modules.hostel.service.HostelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hostels")
@RequiredArgsConstructor
public class HostelController {

    private final HostelService hostelService;

    @PostMapping
    public ResponseEntity<?> createHostel (@Valid @RequestBody HostelCreationDto hostelCreationDto){
        HostelResponseDto hostelResponseDto= hostelService.createHostel(hostelCreationDto);
        return new ResponseEntity<>(hostelResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHostelById(@PathVariable UUID id){
        return new ResponseEntity<>(hostelService.getHostelByid(id), HttpStatus.OK);
    }
}
