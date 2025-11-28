package com.hostelscout.hostel.modules.admin.entity;


import com.hostelscout.hostel.modules.admin.enums.AdminStatus;
import com.hostelscout.hostel.modules.common.entity.BaseUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID admin_id;

    @OneToOne
    @JoinColumn(name = "base_user_id", nullable = false)
    private BaseUser baseUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminStatus adminStatus;

    @Column(name = "status_changed_at")
    private LocalDateTime statusChangedAt;
}
