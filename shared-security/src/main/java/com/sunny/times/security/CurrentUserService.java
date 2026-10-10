package com.sunny.times.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    public UserContext getCurrentUser() {
        return (UserContext)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
    }
}

