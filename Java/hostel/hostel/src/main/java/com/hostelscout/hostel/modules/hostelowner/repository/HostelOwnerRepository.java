package com.hostelscout.hostel.modules.hostelowner.repository;

import com.hostelscout.hostel.modules.hostelowner.entity.HostelOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HostelOwnerRepository extends JpaRepository<HostelOwner, UUID> {
}
