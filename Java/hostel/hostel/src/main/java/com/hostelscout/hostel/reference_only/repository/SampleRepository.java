package com.hostelscout.hostel.reference_only.repository;

import com.hostelscout.hostel.reference_only.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SampleRepository extends JpaRepository<Sample, UUID> {
}
