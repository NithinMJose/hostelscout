package com.hostelscout.hostel.reference_only.repository;

import com.hostelscout.hostel.reference_only.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
}
