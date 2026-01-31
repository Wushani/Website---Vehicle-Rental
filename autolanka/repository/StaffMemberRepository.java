package org.web.autolanka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.autolanka.entity.StaffMember;
import org.web.autolanka.entity.User;

@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {
    Optional<StaffMember> findByUser(User user);
    Optional<StaffMember> findByStaffCode(String staffCode);
    // Using custom query to find by user ID
    @Query("SELECT s FROM StaffMember s WHERE s.user.userId = ?1")
    Optional<StaffMember> findByUserUserId(Long userId);
    boolean existsByStaffCode(String staffCode);
    
    @Modifying
    @Query("DELETE FROM StaffMember s WHERE s.user.userId = ?1")
    void deleteByUserId(Long userId);
}