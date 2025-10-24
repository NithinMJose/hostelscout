package com.hostelscout.hostel.modules.hostel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostelCreationDto {

    @NotBlank(message = "Hostel name is required")
    private String hostelName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Established date is required")
    private LocalDate establishedDate;

    @NotNull(message = "Owner ID is required")
    private UUID ownerId;
}
