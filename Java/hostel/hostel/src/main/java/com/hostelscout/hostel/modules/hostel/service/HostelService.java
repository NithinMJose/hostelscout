package com.hostelscout.hostel.modules.hostel.service;

import com.hostelscout.hostel.common.exception.ResourceConflictException;
import com.hostelscout.hostel.common.exception.ResourceNotFoundException;
import com.hostelscout.hostel.modules.hostel.dto.HostelCreationDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelResponseDto;
import com.hostelscout.hostel.modules.hostel.dto.HostelUpdationDto;
import com.hostelscout.hostel.modules.hostel.entity.Hostel;
import com.hostelscout.hostel.modules.hostel.mapper.HostelMapper;
import com.hostelscout.hostel.modules.hostel.repository.HostelRepository;
import com.hostelscout.hostel.modules.hostelowner.entity.HostelOwner;
import com.hostelscout.hostel.modules.hostelowner.repository.HostelOwnerRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

        // check if the hostel name is already exists
        if(hostelRepository.existsByHostelName(hostelCreationDto.getHostelName())){
            throw new ResourceConflictException("hostel name already exists, try new name");
        }


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

    //LIST_HOSTELS
    @Transactional(readOnly = true)
    public List<HostelResponseDto> listHostels(){
        return hostelRepository.findAll()
                .stream()
                .map(hostelMapper::toHostelResponseDto)
                .toList();
    }

    //UPDATE_HOSTELS
    @Transactional
    public HostelResponseDto updateHostel(HostelUpdationDto hostelUpdationDto){
        //Find the existing Hostel
        Hostel hostel= hostelRepository.findById(hostelUpdationDto.getHostel_id())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to find the hostel with the id : "+ hostelUpdationDto.getHostel_id()));

        //Update the changed fields
        HostelOwner hostelOwner= hostel.getOwner();

        if(hostelUpdationDto.getAddress() != null){
            hostel.setAddress(hostelUpdationDto.getAddress());
        }
        if(hostelUpdationDto.getCity() != null){
            hostel.setCity(hostelUpdationDto.getCity());
        }
        if(hostelUpdationDto.getEstablishedDate() != null){
            hostel.setEstablishedDate(hostelUpdationDto.getEstablishedDate());
        }

        //Save both entities
        hostelOwnerRepository.save(hostelOwner);
        hostelRepository.save(hostel);
        return hostelMapper.toHostelResponseDto(hostel);

    }
}
