package org.web.autolanka.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_executives")
public class FinancialExecutive {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "executive_id")
    private Long executiveId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "employee_code", unique = true, nullable = false, length = 20)
    private String employeeCode;
    
    @Column(nullable = false, length = 50)
    private String department;
    
    @Column(name = "financial_qualification", nullable = false, length = 100)
    private String financialQualification;
    
    @Column(name = "experience_years", nullable = false, length = 20)
    private String experienceYears;
    
    @Column(name = "hire_date")
    private LocalDateTime hireDate = LocalDateTime.now();
    
    // Constructors
    public FinancialExecutive() {}
    
    public FinancialExecutive(User user, String employeeCode, String department, 
                             String financialQualification, String experienceYears) {
        this.user = user;
        this.employeeCode = employeeCode;
        this.department = department;
        this.financialQualification = financialQualification;
        this.experienceYears = experienceYears;
    }
    
    // Getters and Setters
    public Long getExecutiveId() { return executiveId; }
    public void setExecutiveId(Long executiveId) { this.executiveId = executiveId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getFinancialQualification() { return financialQualification; }
    public void setFinancialQualification(String financialQualification) { this.financialQualification = financialQualification; }
    
    public String getExperienceYears() { return experienceYears; }
    public void setExperienceYears(String experienceYears) { this.experienceYears = experienceYears; }
    
    public LocalDateTime getHireDate() { return hireDate; }
    public void setHireDate(LocalDateTime hireDate) { this.hireDate = hireDate; }
}