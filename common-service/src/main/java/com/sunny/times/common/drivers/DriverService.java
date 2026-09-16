package com.sunny.times.common.drivers;

import com.sunny.times.contracts.drivers.DriverDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository repository;

    public DriverService(DriverRepository repository) {
        this.repository = repository;
    }

    public List<DriverDto> getAll() {
        return repository.findAll()
                .stream()
                .map(DriverMapper::toDto)
                .toList();
    }

    public DriverDto getById(String id) {
        DriverEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found: " + id));
        return DriverMapper.toDto(entity);
    }
}
