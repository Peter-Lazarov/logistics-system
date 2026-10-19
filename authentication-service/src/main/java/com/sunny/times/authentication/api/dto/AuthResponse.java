package com.sunny.times.authentication.api.dto;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String role;
    private String message;

    public AuthResponse(String accessToken, String refreshToken, String role) {
        this(accessToken, refreshToken, role, null);
    }

    public AuthResponse(String accessToken, String refreshToken, String role, String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.message = message;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getRole() { return role; }
    public String getMessage() { return message; }
}
