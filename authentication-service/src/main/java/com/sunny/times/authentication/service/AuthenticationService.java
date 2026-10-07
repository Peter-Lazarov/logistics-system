package com.sunny.times.authentication.service;

import com.sunny.times.authentication.api.dto.AuthResponse;
import com.sunny.times.authentication.api.dto.RegisterRequest;
import com.sunny.times.authentication.api.dto.UserResponse;
import com.sunny.times.authentication.model.Role;
import com.sunny.times.authentication.model.User;
import com.sunny.times.authentication.repository.UserRepository;
import com.sunny.times.authentication.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.sunny.times.contracts.clients.ClientDto;

import java.util.List;
import java.util.Set;

@Service
public class AuthenticationService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final WebClient webClient;

    public AuthenticationService(UserRepository repo, PasswordEncoder encoder, JwtUtil jwt, WebClient webClient) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
        this.webClient = webClient;
    }

    public AuthResponse login(String username, String password) {
        User user = repo.findByEmail(username).orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String access = jwt.generateAccessToken(user);
        String refresh = jwt.generateRefreshToken(user);

        return new AuthResponse(access, refresh, user.getRoles().iterator().next().name());
    }

    public AuthResponse register(RegisterRequest request) {
        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.ROLE_CLIENT));

        User savedUser = repo.save(user);
        webClient.post()
                .uri("http://localhost:8081/clients")
                .bodyValue(
                        new ClientDto(
                                savedUser.getId(),
                                null,
                                null,
                                null
                        )
                )
                .retrieve()
                .bodyToMono(ClientDto.class)
                .block();

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
        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.ROLE_EMPLOYEE));
        repo.save(user);

        String access = jwt.generateAccessToken(user);
        String refresh = jwt.generateRefreshToken(user);

        return new AuthResponse(access, refresh, Role.ROLE_EMPLOYEE.name());
    }

    public List<UserResponse> getAllUsers() {

        return repo.findAll().stream().map(user -> new UserResponse(user.getId(), user.getEmail(), user.getRoles().iterator().next().name())).toList();
    }

}
