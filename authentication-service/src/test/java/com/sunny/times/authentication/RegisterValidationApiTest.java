package com.sunny.times.authentication;

import com.sunny.times.authentication.api.AuthenticationController;
import com.sunny.times.authentication.api.GlobalExceptionHandler;
import com.sunny.times.authentication.security.JwtUtil;
import com.sunny.times.authentication.service.AuthenticationService;
import com.sunny.times.authentication.service.SystemResetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class RegisterValidationApiTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AuthenticationController controller = new AuthenticationController(
                mock(AuthenticationService.class),
                mock(JwtUtil.class),
                mock(SystemResetService.class)
        );
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void returnsFieldErrorsForWhitespaceOnlyValues() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"email":"   ","password":"   ","name":"   ","phone":"   "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.email").value("Email is required"))
                .andExpect(jsonPath("$.errors.password").value("Password is required"))
                .andExpect(jsonPath("$.errors.name").value("Full name is required"))
                .andExpect(jsonPath("$.errors.phone").value("Phone number is required"));
    }

    @Test
    void returnsSpecificMessagesForInvalidFormatsAndLengths() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"email":"test","password":"123","name":"John123","phone":"123abc45"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Invalid email format"))
                .andExpect(jsonPath("$.errors.password").value("Password must be between 4 and 50 characters"))
                .andExpect(jsonPath("$.errors.name").value("Full name contains invalid characters"))
                .andExpect(jsonPath("$.errors.phone").value("Invalid phone number format"));
    }

    @Test
    void employeeRegistrationRejectsInvalidRequest() throws Exception {
        mockMvc.perform(post("/auth/admin/register-employee")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"email":"not-an-email","password":"test","name":"John Smith","phone":"0888123456"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Invalid email format"));
    }
}