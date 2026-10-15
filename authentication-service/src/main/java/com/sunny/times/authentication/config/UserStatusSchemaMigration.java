package com.sunny.times.authentication.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserStatusSchemaMigration implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public UserStatusSchemaMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        jdbcTemplate.update("UPDATE users SET status = 'ACTIVE' WHERE status IS NULL");
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN status SET DEFAULT 'ACTIVE'");
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN status SET NOT NULL");
        jdbcTemplate.execute("ALTER TABLE users DROP CONSTRAINT IF EXISTS users_status_check");
    }
}
