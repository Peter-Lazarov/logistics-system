package com.sunny.times.common.routes;

import com.sunny.times.contracts.routes.RouteDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService service;

    public RouteController(RouteService service) {
        this.service = service;
    }

    @GetMapping("/{pathId}")
    public RouteDto getByPathId(@PathVariable String pathId) {
        return service.getByPathId(pathId);
    }
}
