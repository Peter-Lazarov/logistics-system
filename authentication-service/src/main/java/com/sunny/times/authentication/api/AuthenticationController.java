package com.sunny.times.authentication.api;

import com.sunny.times.authentication.api.dto.*;
import com.sunny.times.authentication.security.JwtUtil;
import com.sunny.times.authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<UserResponse>> users() {
        return ResponseEntity.ok(
                auth.getAllUsers()
        );
    }

    @PatchMapping("/admin/users/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        return ResponseEntity.ok(auth.updateUserStatus(id, request));
    }

    @GetMapping("/token/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (authorization == null || !authorization.startsWith("Bearer ")
                || !auth.isActiveAccessToken(authorization.substring(7))) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.noContent().build();
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

    @PutMapping("/me")
    public ResponseEntity<UserProfile> updateProfile(
            HttpServletRequest request,
            @RequestBody UpdateProfileRequest requestDto
    ) {
        String token = request
                .getHeader("Authorization")
                .substring(7);

        String email = jwtUtil.extractEmail(token);

        return ResponseEntity.ok(
                auth.updateProfile(
                        email,
                        requestDto
                )
        );
    }
}
