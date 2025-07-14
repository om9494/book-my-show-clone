package com.bookmyshow.Controllers;

import com.bookmyshow.Enums.Role;
import com.bookmyshow.Models.User;
import com.bookmyshow.Services.JwtService;
import com.bookmyshow.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/signup")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

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

        else{
            System.out.println("Received registration request: " + request);
        }

        Integer age = Integer.parseInt(ageStr);

        Role role = Role.USER;
        System.out.println("RoleStr: " + roleStr);
        if (roleStr != null && roleStr.equalsIgnoreCase("ADMIN")) {
            role = Role.ADMIN;
            System.out.println("Role set to ADMIN for user: " + username);
        }

        //log
        System.out.println("User registration details: " + request);

        String result = userService.registerUser(username, password, name, gender, age,
                phoneNumber, email, Set.of(role));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtService.generateToken(authentication);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", authentication.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            // Handle cases where the user is not authenticated
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);
        if (user == null) {
            // This case can happen if the user is deleted after a token is issued.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}