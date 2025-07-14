package com.bookmyshow.Services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class JwtService {
    public String generateToken(Authentication authentication) {
        // This is NOT a real JWT. It's just a placeholder.
        return Base64.getEncoder().encodeToString(authentication.getName().getBytes());
    }

    public String extractUsername(String token) {
        // This is NOT a real JWT validation. It's just a placeholder.
        // In a real app, you'd verify the signature and extract the subject.
        try {
            return new String(Base64.getDecoder().decode(token));
        } catch (IllegalArgumentException e) {
            return null; // Invalid Base64
        }
    }
}
