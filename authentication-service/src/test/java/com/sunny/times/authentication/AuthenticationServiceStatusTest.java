package com.sunny.times.authentication;

import com.sunny.times.authentication.api.dto.UpdateUserStatusRequest;
import com.sunny.times.authentication.model.Role;
import com.sunny.times.authentication.model.User;
import com.sunny.times.authentication.model.UserStatus;
import com.sunny.times.authentication.repository.UserRepository;
import com.sunny.times.authentication.security.JwtUtil;
import com.sunny.times.authentication.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceStatusTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);
        authenticationService = new AuthenticationService(
                userRepository,
                passwordEncoder,
                jwtUtil,
                mock(WebClient.class)
        );
    }

    @Test
    void disabledUserCannotLogIn() {
        User user = user(UserStatus.DISABLED);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(
                ResponseStatusException.class,
                () -> authenticationService.login(user.getEmail(), "password")
        );
        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void disabledUserCannotRefreshToken() {
        User user = user(UserStatus.DISABLED);
        when(jwtUtil.extractRefreshEmail("refresh-token")).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(
                ResponseStatusException.class,
                () -> authenticationService.refresh("refresh-token")
        );
        verify(jwtUtil, never()).generateAccessToken(any());
    }

    @Test
    void adminCanChangeUserStatus() {
        User user = user(UserStatus.ACTIVE);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        var response = authenticationService.updateUserStatus(
                user.getId(),
                new UpdateUserStatusRequest(UserStatus.DISABLED)
        );

        assertEquals(UserStatus.DISABLED, user.getStatus());
        assertEquals("DISABLED", response.status());
    }

    private User user(UserStatus status) {
        User user = new User();
        user.setId(101L);
        user.setEmail("user@example.com");
        user.setPassword("encoded-password");
        user.setRoles(Set.of(Role.ROLE_CLIENT));
        user.setStatus(status);
        return user;
    }
}
