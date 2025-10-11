package com.hostelscout.hostel.modules.hostel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelOwnerCreationDto {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String companyName;

    @NotBlank
    private String businessRegistrationNumber;

    @NotBlank
    private String contactNumber;


}
