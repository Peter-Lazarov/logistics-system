package com.sunny.times.authentication;

import com.sunny.times.authentication.api.dto.RegisterRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterRequestValidationTest {

    private static jakarta.validation.ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void acceptsDemoPasswordAndNormalizesValues() {
        RegisterRequest request = request("  user@gmail.com  ", " test ", "  John   Smith  ", " +1 (555) 123-4567 ");

        assertTrue(validator.validate(request).isEmpty());
        assertEquals("user@gmail.com", request.getEmail());
        assertEquals("test", request.getPassword());
        assertEquals("John Smith", request.getName());
        assertEquals("+1 (555) 123-4567", request.getPhone());
    }

    @Test
    void rejectsWhitespaceOnlyAndMalformedFields() {
        RegisterRequest request = request("    ", "   ", "      ", "   ");

        Set<String> invalidFields = validator.validate(request).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertEquals(Set.of("email", "password", "name", "phone"), invalidFields);
    }

    @Test
    void rejectsOutOfRangeAndInvalidFormats() {
        RegisterRequest shortPassword = request("user@gmail.com", "123", "John Smith", "0888123456");
        RegisterRequest longPassword = request("user@gmail.com", "p".repeat(51), "John Smith", "0888123456");
        RegisterRequest longEmail = request("a".repeat(101) + "@gmail.com", "test", "John Smith", "0888123456");
        RegisterRequest invalidName = request("user@gmail.com", "test", "John123", "0888123456");
        RegisterRequest longName = request("user@gmail.com", "test", "J".repeat(101), "0888123456");
        RegisterRequest invalidPhone = request("user@gmail.com", "test", "John Smith", "123abc45");
        RegisterRequest longPhone = request("user@gmail.com", "test", "John Smith", "1".repeat(21));

        assertContainsViolation(shortPassword, "password");
        assertContainsViolation(longPassword, "password");
        assertContainsViolation(longEmail, "email");
        assertContainsViolation(invalidName, "name");
        assertContainsViolation(longName, "name");
        assertContainsViolation(invalidPhone, "phone");
        assertContainsViolation(longPhone, "phone");
    }

    @Test
    void acceptsSupportedNameAndPhoneFormats() {
        for (String name : Set.of("Peter", "Ivan Petrov", "Jean-Paul", "O'Connor")) {
            for (String phone : Set.of("+359888123456", "0888123456", "+1 (555) 123-4567")) {
                assertTrue(validator.validate(request("user@gmail.com", "abcd", name, phone)).isEmpty());
            }
        }
    }

    private static RegisterRequest request(String email, String password, String name, String phone) {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        request.setPhone(phone);
        return request;
    }

    private static void assertContainsViolation(RegisterRequest request, String field) {
        assertTrue(validator.validate(request).stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals(field)), field);
    }
}