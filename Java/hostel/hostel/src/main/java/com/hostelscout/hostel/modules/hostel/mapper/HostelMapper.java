package com.hostelscout.hostel.modules.hostel.mapper;

import com.hostelscout.hostel.modules.hostel.dto.HostelResponseDto;
import com.hostelscout.hostel.modules.hostel.entity.Hostel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HostelMapper {

    @Mapping(source = "hostelOwner", target = "owner")
    HostelResponseDto toHostelresponseDto(Hostel hostel);
}
