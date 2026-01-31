package org.web.autolanka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.autolanka.entity.InsuranceOfficer;
import org.web.autolanka.entity.User;

@Repository
public interface InsuranceOfficerRepository extends JpaRepository<InsuranceOfficer, Long> {
    Optional<InsuranceOfficer> findByUser(User user);
    Optional<InsuranceOfficer> findByOfficerCode(String officerCode);
    // Using custom query to find by user ID
    @Query("SELECT i FROM InsuranceOfficer i WHERE i.user.userId = ?1")
    Optional<InsuranceOfficer> findByUserUserId(Long userId);
    boolean existsByOfficerCode(String officerCode);
    
    @Modifying
    @Query("DELETE FROM InsuranceOfficer i WHERE i.user.userId = ?1")
    void deleteByUserId(Long userId);
}