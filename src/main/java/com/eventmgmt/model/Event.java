package com.eventmgmt.model;
import lombok.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;
    private String description;
    private String tags;
    private String location;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime eventDateTime;
    private Boolean isPaid;
    private Double ticketPrice;
    private Integer maxAttendees;
    private String createdBy;
    private String category;
    private String event_img;
    
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    // Event.java
    @ManyToOne
    private OurService service;


    private LocalDateTime createdAt = LocalDateTime.now();
}
