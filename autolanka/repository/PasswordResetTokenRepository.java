// PasswordResetTokenRepository.java
package org.web.autolanka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.web.autolanka.entity.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
    
    void deleteByUser_UserId(Long userId);
    
    @Query("SELECT t FROM PasswordResetToken t WHERE t.user.userId = :userId AND t.token = :token")
    Optional<PasswordResetToken> findByUserIdAndToken(@Param("userId") Long userId, @Param("token") String token);
}