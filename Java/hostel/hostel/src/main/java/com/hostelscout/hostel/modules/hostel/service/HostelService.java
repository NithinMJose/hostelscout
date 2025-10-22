package com.hostelscout.hostel.modules.hostel.service;

import com.hostelscout.hostel.modules.hostel.mapper.HostelMapper;
import com.hostelscout.hostel.modules.hostel.repository.HostelRepository;
import com.hostelscout.hostel.modules.hostelowner.repository.HostelOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HostelService {

    private final HostelRepository hostelRepository;
    private final HostelOwnerRepository hostelOwnerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HostelMapper hostelMapper;

    // CREATE_HOSTEL

}
