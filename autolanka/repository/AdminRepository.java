package org.web.autolanka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.autolanka.entity.Admin;
import org.web.autolanka.entity.User;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUser(User user);
    Optional<Admin> findByAdminCode(String adminCode);
    // Added method to find by user ID
    @Query("SELECT a FROM Admin a WHERE a.user.userId = ?1")
    Optional<Admin> findByUserUserId(Long userId);
    boolean existsByAdminCode(String adminCode);
    
    @Modifying
    @Query("DELETE FROM Admin a WHERE a.user.userId = ?1")
    void deleteByUserId(Long userId);
}