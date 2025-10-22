package com.hostelscout.hostel.modules.hostel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostelCreationDto {

    @NotBlank
    private String hostelName;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private LocalDate establishedDate;

    @NotBlank
    private UUID owner_id;
}
