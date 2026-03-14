package com.eventmgmt.Controller;

import com.eventmgmt.exception.ResourceNotFoundException;
import com.eventmgmt.model.Booking;
import com.eventmgmt.model.Event;
import com.eventmgmt.model.User;
import com.eventmgmt.repository.BookingRepository;
import com.eventmgmt.repository.EventRepository;
import com.eventmgmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // ✅ Ensures all routes are admin-protected
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepo;



    // ✅ 1. Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ 2. Delete a user by ID
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
        return ResponseEntity.ok("✅ User deleted successfully!");
    }

    // ✅ 3. Get all events
    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // ✅ 4. Delete an event by ID
    @DeleteMapping("/events/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        eventRepository.delete(event);
        return ResponseEntity.ok("✅ Event deleted successfully!");
    }


    @GetMapping("/bookings")
    public List<Booking> getAllBookings(Authentication auth) {
        return bookingRepo.findAll();
    }
}
