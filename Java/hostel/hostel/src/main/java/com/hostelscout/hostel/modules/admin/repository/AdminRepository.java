package com.hostelscout.hostel.modules.admin.repository;

import com.hostelscout.hostel.modules.admin.entity.Admin;
import com.hostelscout.hostel.modules.common.entity.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<Admin> findByBaseUser(BaseUser baseUser);
}
