package com.eventmgmt.model;
import lombok.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSVP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rsvpId;

    private Long userId;
    private Long eventId;


    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    private LocalDateTime rsvpTime = LocalDateTime.now();



}
