package com.sunny.times.authentication.api;

import com.sunny.times.authentication.service.AuthenticationService;
import com.sunny.times.authentication.api.dto.LoginRequest;
import com.sunny.times.authentication.api.dto.RegisterRequest;
import com.sunny.times.authentication.api.dto.RefreshRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService auth;

    public AuthenticationController(AuthenticationService auth) {
        this.auth = auth;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(auth.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(auth.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(auth.refresh(request.getToken()));
    }

    @PostMapping("/admin/register-employee")
    public ResponseEntity<?> registerEmployee(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(
                auth.registerEmployee(request)
        );
    }

    @GetMapping("/admin/users")
    public ResponseEntity<?> users() {
        return ResponseEntity.ok(
                auth.getAllUsers()
        );

    }



}
