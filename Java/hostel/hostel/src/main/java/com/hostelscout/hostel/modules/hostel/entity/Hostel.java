package com.hostelscout.hostel.modules.hostel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "hostels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hostel_id;

    @Column(nullable = false)
    private String hostelName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private LocalDate establishedDate;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private HostelOwner owner;

}
