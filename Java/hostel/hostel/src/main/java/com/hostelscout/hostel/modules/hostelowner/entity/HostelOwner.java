package com.hostelscout.hostel.modules.hostelowner.entity;

import com.hostelscout.hostel.modules.common.entity.BaseUser;
import com.hostelscout.hostel.modules.hostel.entity.Hostel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "hostel_owners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID owner_id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = true, unique = true)
    private String businessRegistrationNumber;

    @Column(nullable = false)
    private String contactNumber;

    @OneToOne
    @JoinColumn(name = "base_user_id", nullable = false)
    private BaseUser baseUser;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Hostel> hostels = new ArrayList<>();
}