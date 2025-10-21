package com.hostelscout.hostel.modules.hostelowner.dto;

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
