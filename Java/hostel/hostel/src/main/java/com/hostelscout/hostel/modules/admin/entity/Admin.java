package com.hostelscout.hostel.modules.admin.entity;


import com.hostelscout.hostel.modules.common.entity.BaseUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long admin_id;

    @OneToOne
    @JoinColumn(name = "base_user_id", nullable = false)
    private BaseUser baseUser;
}
