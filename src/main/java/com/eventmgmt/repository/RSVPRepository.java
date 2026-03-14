package com.eventmgmt.repository;

import com.eventmgmt.model.RSVP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RSVPRepository extends JpaRepository<RSVP, Long> {
    List<RSVP> findByUserUserId(Long userId);
    boolean existsByUserIdAndEventId(Long userId, Long eventId);


}
