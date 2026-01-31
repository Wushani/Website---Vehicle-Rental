package org.web.autolanka.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff_members")
public class StaffMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "staff_code", unique = true, nullable = false, length = 20)
    private String staffCode;
    
    @Column(nullable = false, length = 50)
    private String department;
    
    @Column(nullable = false, length = 50)
    private String position;
    
    @Column(name = "hire_date")
    private LocalDateTime hireDate = LocalDateTime.now();
    
    // Constructors
    public StaffMember() {}
    
    public StaffMember(User user, String staffCode, String department, String position) {
        this.user = user;
        this.staffCode = staffCode;
        this.department = department;
        this.position = position;
    }
    
    // Getters and Setters
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getStaffCode() { return staffCode; }
    public void setStaffCode(String staffCode) { this.staffCode = staffCode; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public LocalDateTime getHireDate() { return hireDate; }
    public void setHireDate(LocalDateTime hireDate) { this.hireDate = hireDate; }
}