package com.hostelscout.hostel.reference_only.service;

import com.hostelscout.hostel.reference_only.entity.Sample;
import com.hostelscout.hostel.reference_only.repository.SampleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SampleService {

    private final SampleRepository repo;

    public SampleService(SampleRepository repo) {
        this.repo = repo;
    }

    public List<Sample> getAll() {
        return repo.findAll();
    }

    public Optional<Sample> getById(Long id) {
        return repo.findById(id);
    }

    public Sample create(Sample sample) {
        return repo.save(sample);
    }

    public Optional<Sample> update(Long id, Sample newSample) {
        return repo.findById(id).map(existing -> {
            existing.setName(newSample.getName());
            existing.setDescription(newSample.getDescription());
            return repo.save(existing);
        });
    }


    public boolean delete(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
