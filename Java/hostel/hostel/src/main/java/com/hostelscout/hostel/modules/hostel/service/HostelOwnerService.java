package com.hostelscout.hostel.modules.hostel.service;

import com.hostelscout.hostel.common.exception.ResourceNotFoundException;
import com.hostelscout.hostel.modules.common.entity.BaseUser;
import com.hostelscout.hostel.modules.common.enums.Role;
import com.hostelscout.hostel.common.exception.ResourceConflictException;
import com.hostelscout.hostel.modules.common.repository.BaseUserRepository;
import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerCreationDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerResponseDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerUpdationDto;
import com.hostelscout.hostel.modules.hostel.entity.HostelOwner;
import com.hostelscout.hostel.modules.hostel.mapper.HostelOwnerMapper;
import com.hostelscout.hostel.modules.hostel.repository.HostelOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HostelOwnerService {

    private final HostelOwnerRepository hostelOwnerRepository;
    private final BaseUserRepository baseUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HostelOwnerMapper hostelOwnerMapper;

    // CREATE_HOSTEL_OWNER
    @Transactional
    public HostelOwnerResponseDto createHostelOwner(HostelOwnerCreationDto creationDto) {

        // Check for existing email
        if (baseUserRepository.existsByEmail(creationDto.getEmail())) {
            throw new ResourceConflictException("Email already in use");
        }
        // Check for existing username
        if (baseUserRepository.existsByUsername(creationDto.getUsername())) {
            throw new ResourceConflictException("Username already in use");
        }

        // Create BaseUser
        BaseUser baseUser = BaseUser.builder()
                .username(creationDto.getUsername())
                .email(creationDto.getEmail())
                .password(passwordEncoder.encode(creationDto.getPassword()))
                .role(Role.HOSTEL_OWNER)
                .build();

        baseUser = baseUserRepository.save(baseUser);

        // Create HostelOwner
        HostelOwner hostelOwner = HostelOwner.builder()
                .baseUser(baseUser)
                .companyName(creationDto.getCompanyName())
                .businessRegistrationNumber(creationDto.getBusinessRegistrationNumber())
                .contactNumber(creationDto.getContactNumber())
                .build();

        hostelOwner = hostelOwnerRepository.save(hostelOwner);

        // Prepare and return response DTO
        return hostelOwnerMapper.toResponseDto(hostelOwner);
    }

    //GET_HOSTEL_OWNER
    @Transactional(readOnly = true)
    public HostelOwnerResponseDto getHostelOwnerById(UUID id){
        HostelOwner hostelOwner= hostelOwnerRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("No Owner found with id : "+ id));
        return hostelOwnerMapper.toResponseDto(hostelOwner);
    }

    //LIST_HOSTEL_OWNER
    @Transactional(readOnly = true)
    public List<HostelOwnerResponseDto> listHostelOwners(){
        return hostelOwnerRepository.findAll()
                .stream()
                .map(hostelOwnerMapper::toResponseDto)
                .toList();
    }

    //UPDATE_HOSTEL_OWNER
    @Transactional
    public HostelOwnerResponseDto updateHostelOwner(HostelOwnerUpdationDto hostelOwnerUpdationDto){

        //Find hostel owner by id
        HostelOwner hostelOwner= hostelOwnerRepository.findById(hostelOwnerUpdationDto.getOwner_id())
                .orElseThrow(()-> new ResourceNotFoundException("Cant find Hostel owner with id : "+ hostelOwnerUpdationDto.getOwner_id()));

        //update the related fields
        BaseUser baseUser = hostelOwner.getBaseUser();
        if (hostelOwnerUpdationDto.getCompanyName() != null){
            hostelOwner.setCompanyName(hostelOwnerUpdationDto.getCompanyName());
        }
        if (hostelOwnerUpdationDto.getContactNumber() != null){
            hostelOwner.setContactNumber(hostelOwnerUpdationDto.getContactNumber());
        }
        if (hostelOwnerUpdationDto.getBusinessRegistrationNumber() != null){
            hostelOwner.setBusinessRegistrationNumber(hostelOwnerUpdationDto.getBusinessRegistrationNumber());
        }

        //Save both entities
        baseUserRepository.save(baseUser);
        hostelOwnerRepository.save(hostelOwner);
        return hostelOwnerMapper.toResponseDto(hostelOwner);
    }

}
