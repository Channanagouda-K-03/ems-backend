package com.eventmgmt.Controller;

import com.eventmgmt.model.Event;
import com.eventmgmt.model.RSVP;
import com.eventmgmt.service.RSVPService;
import com.eventmgmt.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/rsvp")
public class RSVPController {

    @Autowired
    private RSVPService rsvpService;

    @Autowired
    private EventRepository eventRepo;

    // Get RSVP confirmation message for an event
    @GetMapping("/{eventId}")
    public String rsvpEvent(@PathVariable Long eventId) {
        Optional<Event> event = eventRepo.findById(eventId);
        return event.map(e -> "RSVP successful for event: " + e.getTitle())
                .orElse("Event not found!");
    }

    // POST RSVP with logged-in user's email extracted from token
    @PostMapping("/rsvp")
    public RSVP rsvpToEvent(@RequestParam Long eventId, Authentication authentication) {
        String email = authentication.getName(); // extract email from JWT
        return rsvpService.addRSVPByEmail(email, eventId);
    }

    // GET all events user has RSVPed to (uses email from Authentication)
    @GetMapping("/my-events")
    public List<RSVP> getMyEvents(Authentication authentication) {
        String email = authentication.getName();
        return rsvpService.getUserRSVPsByEmail(email);
    }
}
