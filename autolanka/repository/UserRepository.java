// UserRepository.java
package org.web.autolanka.repository;

import org.web.autolanka.entity.User;
import org.web.autolanka.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndUserType(String username, UserType userType);

    User findByEmailAndUserType(String email, UserType userType);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findByUserType(UserType userType);

    Optional<User> findByUserId(Long userId);

    long countByUserType(UserType userType);
}
