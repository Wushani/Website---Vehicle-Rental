package org.web.autolanka.repository;

import org.web.autolanka.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    List<Feedback> findByCustomerCustomerId(Long customerId);
    
    List<Feedback> findByBookingBookingId(Long bookingId);
    
    List<Feedback> findByOverallRating(Integer rating);
    
    List<Feedback> findByCreatedDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    
    @Query("SELECT AVG(f.overallRating) FROM Feedback f")
    Double getAverageOverallRating();
    
    @Query("SELECT AVG(f.vehicleConditionRating) FROM Feedback f WHERE f.vehicleConditionRating IS NOT NULL")
    Double getAverageVehicleConditionRating();
    
    @Query("SELECT AVG(f.cleanlinessRating) FROM Feedback f WHERE f.cleanlinessRating IS NOT NULL")
    Double getAverageCleanlinessRating();
    
    @Query("SELECT AVG(f.valueRating) FROM Feedback f WHERE f.valueRating IS NOT NULL")
    Double getAverageValueRating();
    
    @Query("SELECT COUNT(f) FROM Feedback f")
    Long getTotalFeedbackCount();
}