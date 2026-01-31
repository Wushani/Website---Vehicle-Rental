// DeleteRequestDTO.java
package org.web.autolanka.dto;

import jakarta.validation.constraints.NotBlank;

public class DeleteRequestDTO {

    @NotBlank(message = "Email or Username is required")
    private String identifier; // Can be either email or username

    @NotBlank(message = "Password is required")
    private String password;

    private boolean confirmation; // Confirmation checkbox

    // Constructors
    public DeleteRequestDTO() {}

    public DeleteRequestDTO(String identifier, String password, boolean confirmation) {
        this.identifier = identifier;
        this.password = password;
        this.confirmation = confirmation;
    }

    // Getters and Setters
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isConfirmation() { return confirmation; }
    public void setConfirmation(boolean confirmation) { this.confirmation = confirmation; }
}