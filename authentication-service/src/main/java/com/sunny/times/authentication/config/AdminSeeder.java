package com.sunny.times.authentication.config;

import com.sunny.times.authentication.model.Role;
import com.sunny.times.authentication.model.User;
import com.sunny.times.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;

    public AdminSeeder(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        if (repo.findByEmail(adminEmail).isPresent()) {
            return;
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPassword(encoder.encode(adminPassword));
        admin.setRoles(Set.of(Role.ROLE_ADMIN));
        repo.save(admin);
        //System.out.println("Admin user created: " + adminEmail);
    }
}
