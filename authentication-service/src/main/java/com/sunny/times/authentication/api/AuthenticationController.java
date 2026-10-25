package com.sunny.times.authentication.api;

import com.sunny.times.authentication.api.dto.*;
import com.sunny.times.authentication.security.JwtUtil;
import com.sunny.times.authentication.service.AuthenticationService;
import com.sunny.times.authentication.service.SystemResetService;
import com.sunny.times.contracts.common.ApiMessage;
import com.sunny.times.contracts.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService auth;
    private final JwtUtil jwtUtil;
    private final SystemResetService systemResetService;

    public AuthenticationController(
            AuthenticationService auth,
            JwtUtil jwtUtil,
            SystemResetService systemResetService
    ) {
        this.auth = auth;
        this.jwtUtil = jwtUtil;
        this.systemResetService = systemResetService;
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
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "User status updated successfully.",
                auth.updateUserStatus(id, request)
        ));
    }

    @GetMapping("/token/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new ApiMessage("Invalid or expired access token."));
        }
        String token = authorization.substring(7);
        if (!auth.isActiveAccessToken(token)) {
            int status = auth.isDisabledAccessToken(token) ? 403 : 401;
            String message = status == 403
                    ? "Your account has been disabled. Contact an administrator."
                    : "Invalid or expired access token.";
            return ResponseEntity.status(status).body(new ApiMessage(message));
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

//    @PostMapping("/admin/reset")
//    public ApiResponse<Void> reset(
//            HttpServletRequest request
//    ) {
//
//        String token = request
//                .getHeader("Authorization")
//                .substring(7);
//
//        String email = jwtUtil.extractEmail(token);
//
//        if (!email.equals("admin@sunny-times.com")) {
//            throw new ResponseStatusException(
//                    HttpStatus.FORBIDDEN,
//                    "Only the system owner can reset the demo environment."
//            );
//        }
//
//        systemResetService.resetDemoEnvironment();
//
//        return new ApiResponse<>(
//                "Demo environment reset successfully.",
//                null
//        );
//    }

    @PostMapping("/admin/reset")
    public ApiResponse<Void> reset(
            HttpServletRequest request
    ) {

        System.out.println("1");

        String token = request.getHeader("Authorization")
                .substring(7);

        String email = jwtUtil.extractEmail(token);

        System.out.println("2");

        if (!email.equals("admin@sunny-times.com")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only the system owner can reset the demo environment."
            );
        }

        System.out.println("3");

        systemResetService.resetDemoEnvironment();

        System.out.println("4");

        return new ApiResponse<>(
                "Demo environment reset successfully.",
                null
        );
    }

}
