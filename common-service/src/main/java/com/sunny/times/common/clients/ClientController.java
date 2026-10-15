package com.sunny.times.common.clients;

import com.sunny.times.contracts.clients.ClientDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService service;
    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClientDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/me")
    public ClientDto getCurrent() {
        return service.getCurrent();
    }

    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ClientDto create(@RequestBody ClientDto dto) {
        return service.create(dto);
    }

    @PutMapping("/me")
    public ClientDto updateCurrent(@RequestBody ClientDto dto) {
        return service.updateCurrent(dto);
    }

    @PutMapping("/{clientId}")
    public ClientDto update(
            @PathVariable Long clientId,
            @RequestBody ClientDto dto
    ) {
        return service.update(clientId, dto);
    }

}
