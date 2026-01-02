package com.hostelscout.hostel.modules.user.repository;

import com.hostelscout.hostel.modules.common.entity.BaseUser;
import com.hostelscout.hostel.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByBaseUser(BaseUser baseUser);
}

