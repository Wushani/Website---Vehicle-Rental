package org.web.autolanka.service.impl;

import org.web.autolanka.entity.Booking;
import org.web.autolanka.repository.BookingRepository;
import org.web.autolanka.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
    
    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    @Override
    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerCustomerId(customerId);
    }
    
    @Override
    public List<Booking> getBookingsByVehicleId(Long vehicleId) {
        return bookingRepository.findByVehicleVehicleId(vehicleId);
    }
    
    @Override
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByBookingStatus(status);
    }
    
    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    @Override
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setPickupLocation(bookingDetails.getPickupLocation());
            booking.setDropoffLocation(bookingDetails.getDropoffLocation());
            booking.setPickupDate(bookingDetails.getPickupDate());
            booking.setDropoffDate(bookingDetails.getDropoffDate());
            booking.setTotalAmount(bookingDetails.getTotalAmount());
            booking.setBookingStatus(bookingDetails.getBookingStatus());
            booking.setSpecialRequests(bookingDetails.getSpecialRequests());
            booking.setUpdatedDate(java.time.LocalDateTime.now());
            return bookingRepository.save(booking);
        }
        return null;
    }
    
    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    // New method implementation
    @Override
    public List<Booking> getBookingsByCustomerIdAndStatus(Long customerId, String status) {
        return bookingRepository.findByCustomerCustomerIdAndBookingStatus(customerId, status);
    }
}