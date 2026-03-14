package com.eventmgmt.repository;

import com.eventmgmt.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//public interface EventRepository extends JpaRepository<Event, Long> {
//
//    List<Event> findByCreatedBy(String createdBy);
//
//}

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizerUserId(Long userId);
    List<Event> findByCreatedBy(String email);

    @Query("SELECT e FROM Event e " +
            "WHERE (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:keyword IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:after IS NULL OR e.eventDateTime >= :after)")
    Page<Event> findFilteredEvents(@Param("location") String location,
                                   @Param("keyword") String keyword,
                                   @Param("after") LocalDateTime after,
                                   Pageable pageable);

    List<Event> findByService_Name(String name);

}
