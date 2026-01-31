package org.web.autolanka.service;

import org.web.autolanka.entity.Feedback;
import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    
    Feedback saveFeedback(Feedback feedback);
    
    Optional<Feedback> getFeedbackById(Long id);
    
    List<Feedback> getFeedbackByCustomerId(Long customerId);
    
    List<Feedback> getFeedbackByBookingId(Long bookingId);
    
    List<Feedback> getFeedbackByRating(Integer rating);
    
    List<Feedback> getFeedbackByDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    
    List<Feedback> getAllFeedback();
    
    Feedback updateFeedback(Long id, Feedback feedbackDetails);
    
    void deleteFeedback(Long id);
    
    Double getAverageOverallRating();
    
    Double getAverageVehicleConditionRating();
    
    Double getAverageCleanlinessRating();
    
    Double getAverageValueRating();
    
    Long getTotalFeedbackCount();
}