package com.eventmgmt.repository;

import com.eventmgmt.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface AuthService {
    ResponseEntity<?> registerUser(RegisterRequest request);
    ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request);
}
