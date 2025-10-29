package com.hostelscout.hostel.modules.hostel.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostelUpdationDto {

    @NotNull
    private UUID hostel_id;
    private String address;
    private String city;
    private LocalDate establishedDate;
}
