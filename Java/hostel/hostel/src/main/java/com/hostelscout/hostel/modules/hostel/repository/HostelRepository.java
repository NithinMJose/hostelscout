package com.hostelscout.hostel.modules.hostel.repository;

import com.hostelscout.hostel.modules.hostel.entity.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HostelRepository extends JpaRepository<Hostel, UUID> {

}
