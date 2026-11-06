package com.sunny.times.authentication.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$",
            message = "Invalid email format"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 50, message = "Password must be between 4 and 50 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+)*$", message = "Full name contains invalid characters")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Size(min = 7, max = 20, message = "Phone number must contain between 7 and 20 characters")
    @Pattern(regexp = "^[+]?[0-9() -]+$", message = "Invalid phone number format")
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

    public void setEmail(String email) {
        this.email = trim(email);
    }

    public void setPassword(String password) {
        this.password = trim(password);
    }

    public void setName(String name) {
        this.name = name == null ? null : trim(name).replaceAll("[\\s\\p{Z}\\uFEFF]+", " ");
    }

    public void setPhone(String phone) {
        this.phone = trim(phone);
    }

    private static String trim(String value) {
        return value == null ? null : value.replaceAll("^[\\s\\p{Z}\\uFEFF]+|[\\s\\p{Z}\\uFEFF]+$", "");
    }
}
