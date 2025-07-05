package com.bookmyshow.Controllers;

import com.bookmyshow.Enums.Role;
import com.bookmyshow.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/signup")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> request) {
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
    }

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>("Login successful for user: " + authentication.getName(), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>("User: " + authentication.getName() +
                ", Roles: " + authentication.getAuthorities(), HttpStatus.OK);
    }
}