package com.eventmgmt.service;

import com.eventmgmt.model.Event;
import com.eventmgmt.model.RSVP;
import com.eventmgmt.model.User;
import com.eventmgmt.repository.EventRepository;
import com.eventmgmt.repository.RSVPRepository;
import com.eventmgmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RSVPService {

    @Autowired
    private RSVPRepository rsvpRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;

    public RSVP addRSVP(Long userId, Long eventId) {
        if (rsvpRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new RuntimeException("Already RSVPed");
        }

        RSVP rsvp = new RSVP();
        rsvp.setUserId(userId);
        rsvp.setEventId(eventId);
        return rsvpRepository.save(rsvp);
    }

    public List<RSVP> getUserRSVPs(Long userId) {
        return rsvpRepository.findByUserUserId(userId);
    }

//    public RSVP addRSVPByEmail(String email, Long eventId) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new RuntimeException("Event not found"));
//        RSVP rsvp = new RSVP(user, event);
//        return rsvpRepository.save(rsvp);
//    }


    public RSVP addRSVPByEmail(String email, Long eventId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        RSVP rsvp = new RSVP();
        rsvp.setUser(user);
        rsvp.setEvent(event);

        RSVP savedRSVP = rsvpRepository.save(rsvp);

        // ✅ Send confirmation email
        String subject = "RSVP Confirmation: " + event.getTitle();
        String body = "Hi " + user.getEmail() + ",\n\nYou have successfully RSVPed for the event: " + event.getTitle() +
                "\nLocation: " + event.getLocation() +
                "\nDate & Time: " + event.getEventDateTime() + "\n\nThank you!";
        emailService.sendEmail(user.getEmail(), subject, body);

        return savedRSVP;
    }

    public List<RSVP> getUserRSVPsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return rsvpRepository.findByUserUserId(user.getUserId());
    }

}
