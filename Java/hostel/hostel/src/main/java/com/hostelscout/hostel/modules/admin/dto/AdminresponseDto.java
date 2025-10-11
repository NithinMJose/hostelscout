package com.hostelscout.hostel.modules.admin.dto;

import com.hostelscout.hostel.modules.common.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminresponseDto {

    private Long admin_id;
    private String username;
    private String email;
    private Role role;

}
