package com.hostelscout.hostel.modules.hostel.service;

import com.hostelscout.hostel.common.exception.ResourceNotFoundException;
import com.hostelscout.hostel.modules.hostel.dto.HostelCreationDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelResponseDto;
import com.hostelscout.hostel.modules.hostel.entity.Hostel;
import com.hostelscout.hostel.modules.hostel.mapper.HostelMapper;
import com.hostelscout.hostel.modules.hostel.repository.HostelRepository;
import com.hostelscout.hostel.modules.hostelowner.entity.HostelOwner;
import com.hostelscout.hostel.modules.hostelowner.repository.HostelOwnerRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HostelService {

    private final HostelRepository hostelRepository;
    private final HostelOwnerRepository hostelOwnerRepository;
    private final HostelMapper hostelMapper;

    // CREATE_HOSTEL
    @Transactional
    public HostelResponseDto createHostel(HostelCreationDto hostelCreationDto){

        //Check if HostelOwner exists or not
        HostelOwner hostelOwner= hostelOwnerRepository.findById(hostelCreationDto.getOwnerId())
                .orElseThrow( ()-> new ResourceNotFoundException("Hostel Owner doest exists"));

        Hostel hostel = Hostel.builder()
                .hostelName(hostelCreationDto.getHostelName())
                .address(hostelCreationDto.getAddress())
                .city(hostelCreationDto.getCity())
                .establishedDate(hostelCreationDto.getEstablishedDate())
                .owner(hostelOwner)
                .build();

        hostelRepository.save(hostel);

        return hostelMapper.toHostelResponseDto(hostel);
    }

    //GET_HOSTEL_BY_ID
    @Transactional(readOnly = true)
    public HostelResponseDto getHostelByid(UUID id){
        Hostel hostel= hostelRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("No Hostels were found 9with id "+ id));
        return hostelMapper.toHostelResponseDto(hostel);
    }

}
