package com.bookmyshow.Services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String generateToken(Authentication authentication) {
        logger.info("Generating placeholder token for user: {}", authentication.getName());
        // This is NOT a real JWT. It's just a placeholder.
        return Base64.getEncoder().encodeToString(authentication.getName().getBytes());
    }

    public String extractUsername(String token) {
        logger.debug("Attempting to extract username from token...");
        // This is NOT a real JWT validation. It's just a placeholder.
        // In a real app, you'd verify the signature and extract the subject.
        try {
            String username = new String(Base64.getDecoder().decode(token));
            logger.debug("Successfully extracted username: {}", username);
            return username;
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to extract username. Invalid token provided: {}", e.getMessage());
            return null; // Invalid Base64
        }
    }
}