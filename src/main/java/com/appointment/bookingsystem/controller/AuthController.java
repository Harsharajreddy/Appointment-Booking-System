package com.appointment.bookingsystem.controller;

import com.appointment.bookingsystem.dto.LoginRequest;
import com.appointment.bookingsystem.model.User;
import com.appointment.bookingsystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Signup API
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        System.out.println("Received Signup Request: " + user);
        System.out.println("Email: " + user.getEmail()); // Debug log
        return authService.registerUser(user);
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginRequest request) {
        Optional<User> userOpt = authService.findUserByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = authService.loginUser(request.getEmail(), request.getPassword());

            if (token != null) {
                System.out.println("✅ Generated Token: " + token);

                // ✅ Send user details along with the token
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("userId", user.getId());
                response.put("userName", user.getFullName());
                response.put("userEmail", user.getEmail());
                response.put("userPhone", user.getPhone());

                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

}
