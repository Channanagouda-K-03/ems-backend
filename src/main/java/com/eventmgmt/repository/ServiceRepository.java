package com.eventmgmt.repository;

import com.eventmgmt.model.OurService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<OurService, Long> {
    Optional<OurService> findByName(String name);

}
