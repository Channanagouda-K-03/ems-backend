
package com.eventmgmt.Controller;

import com.eventmgmt.exception.ResourceNotFoundException;
import com.eventmgmt.model.Booking;
import com.eventmgmt.model.Event;
import com.eventmgmt.repository.BookingRepository;
import com.eventmgmt.repository.EventRepository;
import com.eventmgmt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizer/events")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    // ✅ Public - Get All Events
    @GetMapping("/public")
    public List<Event> getAllPublicEvents() {
        return eventRepository.findAll();
    }

    // ✅ Create Event
    @PostMapping("/create")
    @PreAuthorize("hasRole('ORGANIZER')")
    public Event createEvent(@RequestBody Event event, Authentication auth) {
        String email = auth.getName();
        event.setCreatedBy(email);
        Event savedEvent = eventRepository.save(event);

        emailService.sendEmail(email, "✅ Event Created: " + event.getTitle(),
                "Your event '" + event.getTitle() + "' was created and scheduled at " + event.getEventDateTime());

        return savedEvent;
    }

    @GetMapping("/my-events")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<Event> myEvents(Authentication auth) {
        return eventRepository.findByCreatedBy(auth.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Event> getEvent(@PathVariable Long id, Authentication auth) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event with ID " + id + " not found"));
        if (!event.getCreatedBy().equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent, Authentication auth) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event with ID " + id + " not found"));

        if (!event.getCreatedBy().equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setLocation(updatedEvent.getLocation());
        event.setEventDateTime(updatedEvent.getEventDateTime());
        eventRepository.save(event);

        List<Booking> bookings = bookingRepository.findByEventId(event.getEventId());
        for (Booking booking : bookings) {
            emailService.sendEmail(
                    booking.getUserEmail(),
                    "📢 Event Updated: " + event.getTitle(),
                    "The event '" + event.getTitle() + "' you registered for has been updated.\nNew time/location: " +
                            event.getEventDateTime() + " at " + event.getLocation()
            );
        }

        return ResponseEntity.ok("✅ Event updated and attendees notified.");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id, Authentication auth) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (!event.getCreatedBy().equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ You are not authorized to delete this event.");
        }

        List<Booking> bookings = bookingRepository.findByEventId(id);
        for (Booking booking : bookings) {
            emailService.sendEmail(
                    booking.getUserEmail(),
                    "❌ Event Cancelled: " + event.getTitle(),
                    "We regret to inform you that the event '" + event.getTitle() + "' has been cancelled."
            );
        }

        eventRepository.deleteById(id);
        return ResponseEntity.ok("✅ Event deleted and attendees notified!");
    }
}

