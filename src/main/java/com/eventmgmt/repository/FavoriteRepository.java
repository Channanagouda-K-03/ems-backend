package com.eventmgmt.repository;

import com.eventmgmt.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Correct property paths: user.userId and event.eventId
    boolean existsByUser_UserIdAndEvent_EventId(Long userId, Long eventId);

    Optional<Favorite> findByUser_UserIdAndEvent_EventId(Long userId, Long eventId);

    List<Favorite> findByUser_UserId(Long userId);

    void deleteByUser_UserIdAndEvent_EventId(Long userId, Long eventId);
}
