package org.web.autolanka.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
public class Admin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "admin_code", unique = true, nullable = false, length = 20)
    private String adminCode;
    
    @Column(name = "admin_level", nullable = false, length = 50)
    private String adminLevel;
    
    @Column(nullable = false, length = 50)
    private String department;
    
    @Column(name = "authorization_level", nullable = false, length = 50)
    private String authorizationLevel;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    // Constructors
    public Admin() {}
    
    public Admin(User user, String adminCode, String adminLevel, String department, String authorizationLevel) {
        this.user = user;
        this.adminCode = adminCode;
        this.adminLevel = adminLevel;
        this.department = department;
        this.authorizationLevel = authorizationLevel;
    }
    
    // Getters and Setters
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getAdminCode() { return adminCode; }
    public void setAdminCode(String adminCode) { this.adminCode = adminCode; }
    
    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getAuthorizationLevel() { return authorizationLevel; }
    public void setAuthorizationLevel(String authorizationLevel) { this.authorizationLevel = authorizationLevel; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}