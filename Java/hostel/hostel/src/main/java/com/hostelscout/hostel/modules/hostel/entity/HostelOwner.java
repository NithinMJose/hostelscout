package com.hostelscout.hostel.modules.hostel.entity;

import com.hostelscout.hostel.modules.common.entity.BaseUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hostel_owners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long owner_id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = true)
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