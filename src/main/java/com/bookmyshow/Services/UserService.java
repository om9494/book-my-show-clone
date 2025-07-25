package com.bookmyshow.Services;

import com.bookmyshow.Enums.Role;
import com.bookmyshow.Models.User;
import com.bookmyshow.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ADD THIS LINE
    @Autowired
    private EmailService emailService;


    public String registerUser(String username, String password, String name, String gender,
                               Integer age, String phoneNumber, String email, Set<Role> roles) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException("Phone number already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, name, gender, age, phoneNumber, email, roles);
        userRepository.save(user);

        return "User registered successfully";
    }

    public String verifyUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            if (authentication.isAuthenticated()) {
                // Get the user details to retrieve their email
                User user = userRepository.findByUsername(username);
                if (user != null) {
                    // ADD THIS LINE TO SEND EMAIL AFTER SUCCESSFUL LOGIN
                    emailService.sendLoginNotificationEmail(user.getEmail(), user.getUsername());
                } else {
                    System.err.println("User not found for username: " + username + ". Cannot send login notification email.");
                }
                return jwtService.generateToken(username);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }

        return null;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}