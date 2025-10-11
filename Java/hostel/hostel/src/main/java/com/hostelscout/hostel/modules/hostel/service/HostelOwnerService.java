package com.hostelscout.hostel.modules.hostel.service;

import com.hostelscout.hostel.modules.common.entity.BaseUser;
import com.hostelscout.hostel.modules.common.enums.Role;
import com.hostelscout.hostel.modules.common.exception.ResourceConflictException;
import com.hostelscout.hostel.modules.common.repository.BaseUserRepository;
import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerCreationDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerResponseDto;
import com.hostelscout.hostel.modules.hostel.entity.HostelOwner;
import com.hostelscout.hostel.modules.hostel.mapper.HostelOwnerMapper;
import com.hostelscout.hostel.modules.hostel.repository.HostelOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HostelOwnerService {

    private final HostelOwnerRepository hostelOwnerRepository;
    private final BaseUserRepository baseUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HostelOwnerMapper hostelOwnerMapper;

    @Transactional
    public HostelOwnerResponseDto createHostelOwner(HostelOwnerCreationDto creationDto) {

        if (baseUserRepository.existsByEmail(creationDto.getEmail())) {
            throw new ResourceConflictException("Email already in use");
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
}
