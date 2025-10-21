package com.hostelscout.hostel.modules.hostelowner.dto;

import com.hostelscout.hostel.modules.common.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelOwnerResponseDto {

    private String username;
    private String email;
    private String companyName;
    private String businessRegistrationNumber;
    private String contactNumber;
    private Role role;

}
