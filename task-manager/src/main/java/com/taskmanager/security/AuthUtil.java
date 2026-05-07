package com.taskmanager.security;

import com.taskmanager.entity.User;
import com.taskmanager.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private final UserRepository userRepo;

    public AuthUtil(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User getLoggedInUser(Authentication auth) {

        String email = auth.getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}