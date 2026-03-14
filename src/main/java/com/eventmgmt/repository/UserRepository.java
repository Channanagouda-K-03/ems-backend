package com.eventmgmt.repository;

import com.eventmgmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // ✅ Spring will automatically implement this method
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}
