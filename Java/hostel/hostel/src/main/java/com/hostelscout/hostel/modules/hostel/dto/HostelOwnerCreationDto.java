package com.hostelscout.hostel.modules.hostel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank
    private String companyName;

    @NotBlank
    private String businessRegistrationNumber;

    @NotBlank
    private String contactNumber;


}
