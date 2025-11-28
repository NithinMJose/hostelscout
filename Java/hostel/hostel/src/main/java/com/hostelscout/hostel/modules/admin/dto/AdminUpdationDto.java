package com.hostelscout.hostel.modules.admin.dto;

import com.hostelscout.hostel.modules.admin.enums.AdminStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUpdationDto {

    private UUID admin_id;
    private AdminStatus adminStatus;
    private String email;

}
