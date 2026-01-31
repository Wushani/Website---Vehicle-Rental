package org.web.autolanka.service.impl;

import org.web.autolanka.entity.Feedback;
import org.web.autolanka.repository.FeedbackRepository;
import org.web.autolanka.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Override
    public Feedback saveFeedback(Feedback feedback) {
        feedback.setUpdatedDate(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }
    
    @Override
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }
    
    @Override
    public List<Feedback> getFeedbackByCustomerId(Long customerId) {
        return feedbackRepository.findByCustomerCustomerId(customerId);
    }
    
    @Override
    public List<Feedback> getFeedbackByBookingId(Long bookingId) {
        return feedbackRepository.findByBookingBookingId(bookingId);
    }
    
    @Override
    public List<Feedback> getFeedbackByRating(Integer rating) {
        return feedbackRepository.findByOverallRating(rating);
    }
    
    @Override
    public List<Feedback> getFeedbackByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return feedbackRepository.findByCreatedDateBetween(startDate, endDate);
    }
    
    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
    
    @Override
    public Feedback updateFeedback(Long id, Feedback feedbackDetails) {
        Optional<Feedback> feedbackOptional = feedbackRepository.findById(id);
        if (feedbackOptional.isPresent()) {
            Feedback feedback = feedbackOptional.get();
            feedback.setOverallRating(feedbackDetails.getOverallRating());
            feedback.setVehicleConditionRating(feedbackDetails.getVehicleConditionRating());
            feedback.setCleanlinessRating(feedbackDetails.getCleanlinessRating());
            feedback.setValueRating(feedbackDetails.getValueRating());
            feedback.setComments(feedbackDetails.getComments());
            feedback.setSuggestions(feedbackDetails.getSuggestions());
            feedback.setUpdatedDate(LocalDateTime.now());
            return feedbackRepository.save(feedback);
        }
        return null;
    }
    
    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
    
    @Override
    public Double getAverageOverallRating() {
        return feedbackRepository.getAverageOverallRating();
    }
    
    @Override
    public Double getAverageVehicleConditionRating() {
        return feedbackRepository.getAverageVehicleConditionRating();
    }
    
    @Override
    public Double getAverageCleanlinessRating() {
        return feedbackRepository.getAverageCleanlinessRating();
    }
    
    @Override
    public Double getAverageValueRating() {
        return feedbackRepository.getAverageValueRating();
    }
    
    @Override
    public Long getTotalFeedbackCount() {
        return feedbackRepository.getTotalFeedbackCount();
    }
}