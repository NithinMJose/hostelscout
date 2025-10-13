package com.hostelscout.hostel.modules.admin.mapper;


import com.hostelscout.hostel.modules.admin.dto.AdminResponseDto;
import com.hostelscout.hostel.modules.admin.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(source = "baseUser.username", target = "username")
    @Mapping(source = "baseUser.email", target = "email")
    @Mapping(source = "baseUser.createdAt", target = "createdAt")
    @Mapping(source = "baseUser.updatedAt", target = "updatedAt")
    @Mapping(source = "baseUser.role", target = "role")
    AdminResponseDto toAdminResponseDto(Admin admin);
}
