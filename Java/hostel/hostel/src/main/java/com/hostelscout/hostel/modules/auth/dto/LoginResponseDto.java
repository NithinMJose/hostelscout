package com.hostelscout.hostel.modules.auth.dto;

import com.hostelscout.hostel.modules.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    private String token;
    private Role role;
    private String username;
    private Object userDetails; // Generic object that can be AdminResponseDto, HostelOwnerResponseDto, or UserResponseDto
}

