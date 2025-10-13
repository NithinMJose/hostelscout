package com.hostelscout.hostel.modules.admin.mapper;


import com.hostelscout.hostel.modules.admin.dto.AdminresponseDto;
import com.hostelscout.hostel.modules.admin.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(source = "admin_id", target = "admin_id")
    @Mapping(source = "baseUser.username", target = "username")
    @Mapping(source = "baseUser.email", target = "email")
    @Mapping(source = "baseUser.role", target = "role")
    AdminresponseDto toAdminesponseDto(Admin admin);
}
