package com.hostelscout.hostel.modules.hostel.controller;

import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerCreationDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerResponseDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerUpdationDto;
import com.hostelscout.hostel.modules.hostel.service.HostelOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/hostel-owners")
@RequiredArgsConstructor
public class HostelOwnerController {

    private final HostelOwnerService hostelOwnerService;

    @PostMapping
    public ResponseEntity<?> createHostelOwner(@Valid @RequestBody HostelOwnerCreationDto creationDto) {
        HostelOwnerResponseDto responseDto = hostelOwnerService.createHostelOwner(creationDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHostelOwnerById(@PathVariable UUID id){
        return new ResponseEntity<>(hostelOwnerService.getHostelOwnerById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<?>> listHostelOwners(){
        List<HostelOwnerResponseDto> owners = hostelOwnerService.listHostelOwners();
        return new ResponseEntity<>(owners, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateHostelOwner(@Valid @RequestBody HostelOwnerUpdationDto hostelOwnerUpdationDto){
        HostelOwnerResponseDto hostelOwnerResponseDto = hostelOwnerService.updateHostelOwner(hostelOwnerUpdationDto);
        return new ResponseEntity<>(hostelOwnerResponseDto, HttpStatus.OK);
    }

}
