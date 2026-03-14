package com.eventmgmt.model;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(
        name = "favorites",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_event", columnNames = {"user_id", "event_id"})
)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // IMPORTANT: match column names to your DB (users.user_id, event.event_id)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public Favorite() {}

    public Favorite(User user, Event event) {
        this.user = user;
        this.event = event;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}
