package com.bookmyshow.Controllers;

import com.bookmyshow.Enums.Role;
import com.bookmyshow.Models.User;
import com.bookmyshow.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/signup")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String name = request.get("name");
            String gender = request.get("gender");
            String ageStr = request.get("age");
            String phoneNumber = request.get("phoneNumber");
            String email = request.get("email");
            String roleStr = request.get("role");

            if (username == null || password == null || name == null || gender == null ||
                    ageStr == null || phoneNumber == null || email == null) {
                return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
            }

            Integer age = Integer.parseInt(ageStr);

            Role role = Role.USER;
            if (roleStr != null && roleStr.equalsIgnoreCase("ADMIN")) {
                role = Role.ADMIN;
            }

            String result = userService.registerUser(username, password, name, gender, age,
                    phoneNumber, email, Set.of(role));
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            if (username == null || password == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username and password are required");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

            String token = userService.verifyUser(username, password);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("username", username);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Map<String, Object> response = new HashMap<>();
        // response.put("username", authentication.getName());
        // response.put("roles", authentication.getAuthorities());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}