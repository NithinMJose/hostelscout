package com.hostelscout.hostel.reference_only.controller;

import com.hostelscout.hostel.reference_only.entity.Sample;
import com.hostelscout.hostel.reference_only.service.SampleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sample")
public class SampleController {

    private final SampleService service;

    public SampleController(SampleService service) {
        this.service = service;
    }

    @GetMapping
    public List<Sample> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sample> getById(@PathVariable UUID id) {
        return service.getById(id)
                .map(sample -> new ResponseEntity<>(sample, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Sample> create(@RequestBody Sample sample) {
        Sample saved = service.create(sample);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sample> update(@PathVariable UUID id, @RequestBody Sample newSample) {
        return service.update(id, newSample)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (service.delete(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
