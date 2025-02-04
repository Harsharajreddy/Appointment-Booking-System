package com.appointment.bookingsystem.service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.appointment.bookingsystem.model.User;
import com.appointment.bookingsystem.repository.UserRepository;
import com.appointment.bookingsystem.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered!"); // ✅ Send 400 error
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }

        userRepository.save(user);
        return "User registered successfully!";
    }
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public String loginUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElse(null);

        if (user == null) {
            return "User not found!";
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Invalid credentials!";
        }

        // ✅ Convert Role Enum to String
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return "{ \"token\": \"" + token + "\" }";
    }
}
