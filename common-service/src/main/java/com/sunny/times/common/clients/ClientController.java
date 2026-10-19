package com.sunny.times.common.clients;

import com.sunny.times.contracts.clients.ClientDto;
import com.sunny.times.contracts.common.ApiResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<ClientDto>> create(@RequestBody ClientDto dto) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Client created successfully.",
                service.create(dto)
        ));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ClientDto>> updateCurrent(@RequestBody ClientDto dto) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Client updated successfully.",
                service.updateCurrent(dto)
        ));
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ApiResponse<ClientDto>> update(
            @PathVariable Long clientId,
            @RequestBody ClientDto dto
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Client updated successfully.",
                service.update(clientId, dto)
        ));
    }

}
