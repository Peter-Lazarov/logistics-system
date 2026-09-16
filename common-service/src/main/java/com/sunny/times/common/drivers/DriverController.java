package com.sunny.times.common.drivers;

import com.sunny.times.contracts.drivers.DriverDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService service;

    public DriverController(DriverService service) {
        this.service = service;
    }

    @GetMapping
    public List<DriverDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public DriverDto getById(@PathVariable String id) {
        return service.getById(id);
    }
}
