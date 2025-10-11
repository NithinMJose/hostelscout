package com.hostelscout.hostel.modules.admin.repository;

import com.hostelscout.hostel.modules.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

}
