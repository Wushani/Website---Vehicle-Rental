package org.web.autolanka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.autolanka.entity.FinancialExecutive;
import org.web.autolanka.entity.User;

@Repository
public interface FinancialExecutiveRepository extends JpaRepository<FinancialExecutive, Long> {
    Optional<FinancialExecutive> findByUser(User user);
    Optional<FinancialExecutive> findByEmployeeCode(String employeeCode);
    // Added method to find by user ID
    @Query("SELECT f FROM FinancialExecutive f WHERE f.user.userId = ?1")
    Optional<FinancialExecutive> findByUserUserId(Long userId);
    boolean existsByEmployeeCode(String employeeCode);
    
    @Modifying
    @Query("DELETE FROM FinancialExecutive f WHERE f.user.userId = ?1")
    void deleteByUserId(Long userId);
}