// UserResponseDTO.java
package org.web.autolanka.dto;

import org.web.autolanka.enums.UserType;

public class UserResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private UserType userType;

    // Constructors
    public UserResponseDTO() {}

    public UserResponseDTO(Long userId, String username, String email, String fullName, UserType userType) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
}