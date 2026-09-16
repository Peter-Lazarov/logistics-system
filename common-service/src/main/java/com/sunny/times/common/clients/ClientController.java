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

    @GetMapping("/{clientId}")
    public ClientDto getById(@PathVariable String clientId) {
        return service.getById(clientId);
    }
}
