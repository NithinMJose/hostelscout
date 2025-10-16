package com.hostelscout.hostel.modules.hostel.mapper;

import com.hostelscout.hostel.modules.hostel.dto.HostelOwnerResponseDto;
import com.hostelscout.hostel.modules.hostel.entity.HostelOwner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HostelOwnerMapper {

    @Mapping(source = "baseUser.role", target = "role")
    @Mapping(source = "baseUser.username", target = "username")
    @Mapping(source = "baseUser.email", target = "email")
    HostelOwnerResponseDto toResponseDto(HostelOwner hostelOwner);
}
