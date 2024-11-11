package com.example.eproject4.controller;

import com.example.eproject4.model.User;
import com.example.eproject4.service.UserService;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            registeredUser.setPassword(null);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> user = userService.login(email, password);
        if (user.isPresent()) {
            User loggedInUser = user.get();
            loggedInUser.setPassword(null);
            return ResponseEntity.ok(loggedInUser);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser, @RequestParam("userId") int userId) {
        try {
            User user = userService.updateProfile(updatedUser, userId);
            user.setPassword(null); // Do not return password
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> passwords, @RequestParam("userId") int userId) {
        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");

        try {
            User updatedUser = userService.updatePassword(userId, oldPassword, newPassword);
            updatedUser.setPassword(null); // Do not return password
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            // Return 401 if the old password is incorrect
            return ResponseEntity.status(401).body("Incorrect old password.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Password update failed: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        // Optionally, you can remove the password field for all users before returning the response
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }
}