package com.hostelscout.hostel.modules.hostel.dto;

import com.hostelscout.hostel.modules.hostelowner.dto.HostelOwnerResponseDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostelResponseDto {

    private String hostelName;
    private String address;
    private String city;
    private String establishedDate;
    private HostelOwnerResponseDto owner;

}
