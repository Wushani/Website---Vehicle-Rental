package org.web.autolanka.repository;

import org.web.autolanka.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerCustomerId(Long customerId);
    List<Booking> findByVehicleVehicleId(Long vehicleId);
    List<Booking> findByBookingStatus(String bookingStatus);
    List<Booking> findByCustomerCustomerIdAndBookingStatus(Long customerId, String bookingStatus);
    
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.vehicle.vehicleId = ?1")
    void deleteByVehicleVehicleId(Long vehicleId);
}