package com.sunny.times.authentication.service;

import com.sunny.times.authentication.api.dto.AuthResponse;
import com.sunny.times.authentication.api.dto.RegisterRequest;
import com.sunny.times.authentication.model.Role;
import com.sunny.times.authentication.model.User;
import com.sunny.times.authentication.repository.UserRepository;
import com.sunny.times.authentication.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthenticationService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthenticationService(UserRepository repo, PasswordEncoder encoder, JwtUtil jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public AuthResponse login(String username, String password) {
        User user = repo.findByUsername(username).orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String access = jwt.generateAccessToken(user);
        String refresh = jwt.generateRefreshToken(user);

        return new AuthResponse(access, refresh, user.getRoles().iterator().next().name());
    }

    public AuthResponse register(RegisterRequest request) {
        if (repo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.ROLE_CLIENT));

        repo.save(user);

        String access = jwt.generateAccessToken(user);
        String refresh = jwt.generateRefreshToken(user);

        return new AuthResponse(access, refresh, user.getRoles().iterator().next().name());
    }

    public AuthResponse refresh(String refreshToken) {
        User user = jwt.validateRefreshToken(refreshToken);
        String newAccess = jwt.generateAccessToken(user);
        String newRefresh = jwt.generateRefreshToken(user); // по желание, rotation

        return new AuthResponse(newAccess, newRefresh, user.getRoles().iterator().next().name());
    }

    public AuthResponse registerEmployee(RegisterRequest request) {
        if (repo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.ROLE_EMPLOYEE));
        repo.save(user);

        String access = jwt.generateAccessToken(user);
        String refresh = jwt.generateRefreshToken(user);

        return new AuthResponse(access, refresh, Role.ROLE_EMPLOYEE.name());
    }

}
