package com.eventmgmt.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey;
    private Key key;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new RuntimeException("JWT secret is missing!");
        }

        try {
            // ✅ Decode Base64 secret properly
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT secret format!", e);
        }
    }

    // Generate token with role
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hrs
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract all claims
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check validity
    public boolean isTokenValid(String token, String email) {
        return extractUsername(token).equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}