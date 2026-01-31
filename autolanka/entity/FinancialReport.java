package org.web.autolanka.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_reports")
public class FinancialReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;
    
    @Column(name = "report_name", length = 100, nullable = false)
    private String reportName;
    
    @Column(name = "report_type", length = 50)
    private String reportType; // REVENUE, EXPENSE, PROFIT_LOSS, BALANCE_SHEET
    
    @Column(name = "report_period_start")
    private LocalDateTime reportPeriodStart;
    
    @Column(name = "report_period_end")
    private LocalDateTime reportPeriodEnd;
    
    @Column(name = "total_revenue", precision = 12, scale = 2)
    private BigDecimal totalRevenue;
    
    @Column(name = "total_expenses", precision = 12, scale = 2)
    private BigDecimal totalExpenses;
    
    @Column(name = "net_profit", precision = 12, scale = 2)
    private BigDecimal netProfit;
    
    @Column(name = "generated_by", length = 100)
    private String generatedBy;
    
    @Column(name = "report_data", columnDefinition = "NVARCHAR(MAX)")
    private String reportData; // JSON data for the report
    
    @Column(name = "generated_date")
    private LocalDateTime generatedDate = LocalDateTime.now();
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();
    
    // Constructors
    public FinancialReport() {}
    
    public FinancialReport(String reportName, String reportType, LocalDateTime reportPeriodStart, 
                          LocalDateTime reportPeriodEnd, String generatedBy) {
        this.reportName = reportName;
        this.reportType = reportType;
        this.reportPeriodStart = reportPeriodStart;
        this.reportPeriodEnd = reportPeriodEnd;
        this.generatedBy = generatedBy;
    }
    
    // Getters and Setters
    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }
    
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    
    public LocalDateTime getReportPeriodStart() { return reportPeriodStart; }
    public void setReportPeriodStart(LocalDateTime reportPeriodStart) { this.reportPeriodStart = reportPeriodStart; }
    
    public LocalDateTime getReportPeriodEnd() { return reportPeriodEnd; }
    public void setReportPeriodEnd(LocalDateTime reportPeriodEnd) { this.reportPeriodEnd = reportPeriodEnd; }
    
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }
    
    public BigDecimal getNetProfit() { return netProfit; }
    public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }
    
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    
    public String getReportData() { return reportData; }
    public void setReportData(String reportData) { this.reportData = reportData; }
    
    public LocalDateTime getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDateTime generatedDate) { this.generatedDate = generatedDate; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}