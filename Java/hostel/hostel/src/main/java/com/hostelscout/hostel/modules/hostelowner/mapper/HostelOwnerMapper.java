package com.hostelscout.hostel.modules.hostelowner.mapper;

import com.hostelscout.hostel.modules.hostelowner.dto.HostelOwnerResponseDto;
import com.hostelscout.hostel.modules.hostelowner.entity.HostelOwner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HostelOwnerMapper {

    @Mapping(source = "baseUser.role", target = "role")
    @Mapping(source = "baseUser.username", target = "username")
    @Mapping(source = "baseUser.email", target = "email")
    HostelOwnerResponseDto toResponseDto(HostelOwner hostelOwner);
}
