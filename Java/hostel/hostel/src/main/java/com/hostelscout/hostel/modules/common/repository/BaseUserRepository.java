package com.hostelscout.hostel.modules.common.repository;

import com.hostelscout.hostel.modules.common.entity.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<BaseUser> findByUsername(String username);

}
