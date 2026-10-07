package com.sunny.times.authentication.api.dto;

public class RegisterRequest {

    private String email;
    private String password;

    private String name;
    private String phone;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
