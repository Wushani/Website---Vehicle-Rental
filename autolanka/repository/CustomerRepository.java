package org.web.autolanka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.autolanka.entity.Customer;
import org.web.autolanka.entity.User;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUser(User user);
    Optional<Customer> findByNic(String nic);
    boolean existsByNic(String nic);
    
    @Query("SELECT c FROM Customer c WHERE c.user.userId = ?1")
    Optional<Customer> findByUserId(Long userId);
    
    @Modifying
    @Query("DELETE FROM Customer c WHERE c.user.userId = ?1")
    void deleteByUserId(Long userId);
}