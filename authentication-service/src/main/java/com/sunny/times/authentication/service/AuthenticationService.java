package com.sunny.times.authentication.service;

import com.sunny.times.authentication.api.dto.*;
import com.sunny.times.authentication.mapper.UserMapper;
import com.sunny.times.authentication.model.Role;
import com.sunny.times.authentication.model.User;
import com.sunny.times.authentication.model.UserStatus;
import com.sunny.times.authentication.repository.UserRepository;
import com.sunny.times.authentication.security.JwtUtil;
import com.sunny.times.contracts.clients.ClientDto;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class AuthenticationService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final WebClient webClient;
    private final String commonServiceUrl;

    public AuthenticationService(
            UserRepository repo,
            PasswordEncoder encoder,
            JwtUtil jwt,
            WebClient webClient,
            @Value("${common-service.url:http://localhost:8081}") String commonServiceUrl
    ) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
        this.webClient = webClient;
        this.commonServiceUrl = commonServiceUrl;
    }

    public AuthResponse login(String username, String password) {
        User user = repo.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password."
                ));

        ensureActive(user);
        if (!encoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }

        String access = jwt.generateAccessToken(user);
        String refresh = jwt.generateRefreshToken(user);

        return new AuthResponse(access, refresh, user.getRoles().iterator().next().name(), "Login successful.");
    }

    public AuthResponse register(RegisterRequest request) {
        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A user with this email already exists."
            );
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setRoles(Set.of(Role.ROLE_CLIENT));

        User savedUser = repo.save(user);
        webClient.post()
                .uri(commonServiceUrl + "/clients")
                .bodyValue(
                        new ClientDto(
                                savedUser.getId(),
                                null,
                                null,
                                null
                        )
                )
                .retrieve()
                .toBodilessEntity()
                .block();

        String access = jwt.generateAccessToken(user);
        String refresh = jwt.generateRefreshToken(user);

        return new AuthResponse(
                access,
                refresh,
                user.getRoles().iterator().next().name(),
                "User registered successfully."
        );
    }

    public AuthResponse refresh(String refreshToken) {
        String email;
        try {
            email = jwt.extractRefreshEmail(refreshToken);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token.");
        }

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid or expired refresh token."
                ));
        ensureActive(user);
        String newAccess = jwt.generateAccessToken(user);
        String newRefresh = jwt.generateRefreshToken(user); // по желание, rotation

        return new AuthResponse(
                newAccess,
                newRefresh,
                user.getRoles().iterator().next().name(),
                "Token refreshed successfully."
        );
    }

    public AuthResponse registerEmployee(RegisterRequest request) {
        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A user with this email already exists."
            );
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setRoles(Set.of(Role.ROLE_EMPLOYEE));
        repo.save(user);

        String access = jwt.generateAccessToken(user);
        String refresh = jwt.generateRefreshToken(user);

        return new AuthResponse(
                access,
                refresh,
                Role.ROLE_EMPLOYEE.name(),
                "Employee registered successfully."
        );
    }

    public List<UserResponse> getAllUsers() {
        return repo.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        user.setStatus(request.status());
        return UserMapper.toResponse(repo.save(user));
    }

    public boolean isActiveAccessToken(String accessToken) {
        String email;
        try {
            email = jwt.extractEmail(accessToken);
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException exception) {
            return false;
        }

        return repo.findByEmail(email)
                .map(user -> user.getStatus() == UserStatus.ACTIVE)
                .orElse(false);
    }

    public boolean isDisabledAccessToken(String accessToken) {
        String email;
        try {
            email = jwt.extractEmail(accessToken);
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException exception) {
            return false;
        }

        return repo.findByEmail(email)
                .map(user -> user.getStatus() == UserStatus.DISABLED)
                .orElse(false);
    }

    public UserProfile getUserById(Long id) {
        User user = repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")
        );

        return new UserProfile(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public UserProfile getCurrentUser(String email) {
        User user = repo.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")
                );

        return new UserProfile(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public UserProfile updateProfile(
            String email,
            UpdateProfileRequest request
    ) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found."
                ));

        user.setName(request.name());
        user.setPhone(request.phone());

        User updatedUser = repo.save(user);

        return new UserProfile(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPhone()
        );
    }

    private void ensureActive(User user) {
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your account has been disabled. Contact an administrator."
            );
        }
    }
}
