// DatabaseTestController.java
package org.web.autolanka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.autolanka.entity.User;
import org.web.autolanka.enums.UserType;
import org.web.autolanka.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class DatabaseTestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public Map<String, Object> testUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userRepository.findAll();
            response.put("success", true);
            response.put("totalUsers", users.size());
            response.put("users", users.stream().map(user -> {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getUserId());
                userInfo.put("username", user.getUsername());
                userInfo.put("email", user.getEmail());
                userInfo.put("fullName", user.getFullName());
                userInfo.put("userType", user.getUserType());
                userInfo.put("status", user.getStatus());
                userInfo.put("createdDate", user.getCreatedDate());
                return userInfo;
            }).toList());
            
            // Add user type counts
            Map<String, Long> userTypeCounts = new HashMap<>();
            for (UserType type : UserType.values()) {
                userTypeCounts.put(type.name(), userRepository.countByUserType(type));
            }
            response.put("userTypeCounts", userTypeCounts);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/database")
    public Map<String, Object> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalUsers = userRepository.count();
            response.put("success", true);
            response.put("message", "Database connection successful");
            response.put("totalUsers", totalUsers);
            response.put("timestamp", java.time.LocalDateTime.now());
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
}