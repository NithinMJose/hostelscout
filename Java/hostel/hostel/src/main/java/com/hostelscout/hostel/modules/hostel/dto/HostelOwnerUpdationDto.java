package com.hostelscout.hostel.modules.hostel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelOwnerUpdationDto {

    @NotNull
    private UUID owner_id;

    private String companyName;
    private String businessRegistrationNumber;
    private String contactNumber;

}
