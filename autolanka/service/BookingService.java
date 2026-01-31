package org.web.autolanka.service;

import org.web.autolanka.entity.Booking;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking saveBooking(Booking booking);
    Optional<Booking> getBookingById(Long id);
    List<Booking> getBookingsByCustomerId(Long customerId);
    List<Booking> getBookingsByVehicleId(Long vehicleId);
    List<Booking> getBookingsByStatus(String status);
    List<Booking> getAllBookings();
    Booking updateBooking(Long id, Booking bookingDetails);
    void deleteBooking(Long id);
    
    // New method to get bookings by customer ID and status
    List<Booking> getBookingsByCustomerIdAndStatus(Long customerId, String status);
}