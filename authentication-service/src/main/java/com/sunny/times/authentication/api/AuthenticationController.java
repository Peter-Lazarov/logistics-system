package com.sunny.times.authentication.api;

import com.sunny.times.authentication.api.dto.UserProfile;
import com.sunny.times.authentication.security.JwtUtil;
import com.sunny.times.authentication.service.AuthenticationService;
import com.sunny.times.authentication.api.dto.LoginRequest;
import com.sunny.times.authentication.api.dto.RegisterRequest;
import com.sunny.times.authentication.api.dto.RefreshRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService auth;
    private final JwtUtil jwtUtil;

    public AuthenticationController(
            AuthenticationService auth,
            JwtUtil jwtUtil
    ) {
        this.auth = auth;
        this.jwtUtil = jwtUtil;
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

    @GetMapping("/users/{id}")
    public ResponseEntity<UserProfile> getUserById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                auth.getUserById(id)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfile> me(
            HttpServletRequest request
    ) {
        String token = request
                .getHeader("Authorization")
                .substring(7);

        String email = jwtUtil.extractEmail(token);

        return ResponseEntity.ok(
                auth.getCurrentUser(email)
        );
    }
}
