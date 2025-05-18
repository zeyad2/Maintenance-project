package com.app.lms.user_management.service;

import com.app.lms.user_management.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.lms.config.JwtConfig;

@Service
public class AuthService {

    private final UserService userService; // Assume UserService fetches user details from DB
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtConfig jwtConfig) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
    }

    public String authenticate(String username, String password) {
        User user = userService.findByUsername(username);

        // Validate if user exists
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        // Validate password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT token
        return jwtConfig.generateToken(user.getId(), user.getRole().name());
    }
}
