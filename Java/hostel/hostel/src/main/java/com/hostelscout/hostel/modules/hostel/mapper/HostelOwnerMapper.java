package com.hostelscout.hostel.modules.hostel.mapper;

import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerResponseDto;
import com.hostelscout.hostel.modules.hostel.entity.HostelOwner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HostelOwnerMapper {

    @Mapping(source = "owner_id", target = "owner_id")
    @Mapping(source = "baseUser.role", target = "role")
    HostelOwnerResponseDto toResponseDto(HostelOwner hostelOwner);
}
