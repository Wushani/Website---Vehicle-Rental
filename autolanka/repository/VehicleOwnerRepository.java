package org.web.autolanka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.autolanka.entity.VehicleOwner;
import org.web.autolanka.entity.User;

import java.util.Optional;

@Repository
public interface VehicleOwnerRepository extends JpaRepository<VehicleOwner, Long> {
    Optional<VehicleOwner> findByUser(User user);
    Optional<VehicleOwner> findByNic(String nic);
    boolean existsByNic(String nic);
    
    @Modifying
    @Query("DELETE FROM VehicleOwner v WHERE v.user.userId = ?1")
    void deleteByUserId(Long userId);
    
    @Query("SELECT v FROM VehicleOwner v JOIN FETCH v.user WHERE v.ownerId = ?1")
    Optional<VehicleOwner> findWithUserById(Long ownerId);
    
    @Query("SELECT v FROM VehicleOwner v JOIN FETCH v.user WHERE v.user.userId = ?1")
    Optional<VehicleOwner> findByUserId(Long userId);
}