package com.hostelscout.hostel.modules.hostel.dto;

import com.hostelscout.hostel.modules.common.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelOwnerResponseDto {

    private Long owner_id;
    private String companyName;
    private String businessRegistrationNumber;
    private String contactNumber;
    private Role role;

}
