package com.hostelscout.hostel.modules.hostel.mapper;

import com.hostelscout.hostel.modules.hostel.dto.HostelResponseDto;
import com.hostelscout.hostel.modules.hostel.entity.Hostel;
import com.hostelscout.hostel.modules.hostelowner.mapper.HostelOwnerMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = HostelOwnerMapper.class)
public interface HostelMapper {

    HostelMapper INSTANCE = Mappers.getMapper(HostelMapper.class);

    @Mapping(source = "hostelName", target = "hostelName")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "establishedDate", target = "establishedDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "owner", target = "owner")
    HostelResponseDto toHostelResponseDto(Hostel hostel);
}
