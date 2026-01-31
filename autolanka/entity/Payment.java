package org.web.autolanka.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(name = "payment_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal paymentAmount;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // CARD_PAYMENT, BANK_TRANSFER_SLIP_UPLOAD
    
    @Column(name = "payment_status", length = 20)
    private String paymentStatus = "PENDING"; // PENDING, COMPLETED, FAILED, REFUNDED, APPROVED, REJECTED
    
    @Column(name = "transaction_id", length = 100, unique = true)
    private String transactionId;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate = LocalDateTime.now();
    
    @Column(name = "refund_date")
    private LocalDateTime refundDate;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();
    
    // New fields for bank slip functionality
    @Column(name = "bank_slip_path", length = 255)
    private String bankSlipPath;
    
    @Column(name = "approval_status", length = 20)
    private String approvalStatus; // PENDING, APPROVED, REJECTED
    
    @Column(name = "approval_date")
    private LocalDateTime approvalDate;
    
    @Column(name = "approved_by")
    private String approvedBy;
    
    // Constructors
    public Payment() {}
    
    public Payment(Booking booking, BigDecimal paymentAmount, String paymentMethod) {
        this.booking = booking;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters and Setters
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    
    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public LocalDateTime getRefundDate() { return refundDate; }
    public void setRefundDate(LocalDateTime refundDate) { this.refundDate = refundDate; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
    
    // Getters and Setters for new bank slip fields
    public String getBankSlipPath() { return bankSlipPath; }
    public void setBankSlipPath(String bankSlipPath) { this.bankSlipPath = bankSlipPath; }
    
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    
    public LocalDateTime getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDateTime approvalDate) { this.approvalDate = approvalDate; }
    
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
}