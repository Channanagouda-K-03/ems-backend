package com.eventmgmt.Controller;

import com.eventmgmt.dto.*;
import com.eventmgmt.model.User;
import com.eventmgmt.repository.AuthService;
import com.eventmgmt.repository.UserRepository;
import com.eventmgmt.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthService authService;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
//        return authService.registerUser(request);
//    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        // Optional: Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "User already exists!";
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "USER");

        userRepository.save(user);
        return "User registered successfully!";
    }



        @PostMapping("/forgot-password")
        public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
            try {
                // Log email for debug
                System.out.println("Forgot password requested for: " + request.getEmail());

                // Dummy logic: find user or simulate
                if (request.getEmail() == null || request.getEmail().isEmpty()) {
                    return ResponseEntity.badRequest().body("Email is required");
                }

                // Here you can send email or save token
                return ResponseEntity.ok("Reset email sent (mock)");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Server error in forgot-password");
            }
        }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate using Spring Security
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Get user from DB
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow();

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

            // ✅ Return both token and role
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole());  // "USER", "ADMIN", "ORGANIZER"

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        } catch (Exception e) {
            e.printStackTrace();  // Debugging
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }



    }





}
