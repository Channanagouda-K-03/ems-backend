package com.eventmgmt.Controller;

import com.eventmgmt.exception.ResourceNotFoundException;
import com.eventmgmt.model.Booking;
import com.eventmgmt.model.Event;
import com.eventmgmt.model.User;
import com.eventmgmt.repository.BookingRepository;
import com.eventmgmt.repository.EventRepository;
import com.eventmgmt.repository.UserRepository;
import com.eventmgmt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private EmailService emailService;

    /** ✅ Register for an event */
    @PostMapping("/{eventId}")
    public ResponseEntity<?> bookEvent(@PathVariable Long eventId, Authentication auth) {
        try {
            String email = auth.getName();
            System.out.println("Booking event ID: " + eventId + " by " + email);

            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Event event = eventRepo.findById(eventId)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            if (bookingRepo.existsByUserIdAndEventId(user.getUserId(), eventId)) {
                return ResponseEntity.badRequest().body("Already registered for this event.");
            }

            Booking booking = new Booking();
            booking.setUserId(user.getUserId());
            booking.setUserEmail(user.getEmail());
            booking.setEventId(eventId);
            booking.setStatus("REGISTERED");
            booking.setBookingTime(LocalDateTime.now());

            bookingRepo.save(booking);

            // Send notification email (optional)
            try {
                emailService.sendEmail(
                        event.getCreatedBy(),
                        "📢 New Registration for: " + event.getTitle(),
                        "👤 " + user.getUsername() + " has registered for your event \"" + event.getTitle() +
                                "\" scheduled on " + event.getEventDateTime()
                );
            } catch (Exception e) {
                System.err.println("❌ Failed to send registration email: " + e.getMessage());
            }

            return ResponseEntity.ok("✅ Registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/my/{userId}")
    public ResponseEntity<List<Booking>> getMyBookings(@PathVariable Long userId) {
        List<Booking> bookings = bookingRepo.findByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    /** ✅ Get logged-in user's bookings */
    @GetMapping("/bookings/my/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        List<Booking> bookings = bookingRepo.findByUserId(userId);

        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(bookings);
    }


    /** ✅ Cancel a booking */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId, Authentication auth) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUserEmail().equals(auth.getName())) {
            return ResponseEntity.status(403).body("❌ Unauthorized to cancel this booking");
        }

        booking.setStatus("CANCELLED");
        bookingRepo.save(booking);

        // Send cancellation email (optional)
        try {
            emailService.sendEmail(
                    booking.getUserEmail(),
                    "📭 Booking Cancelled",
                    "Your booking for Event ID " + booking.getEventId() + " has been cancelled successfully."
            );
        } catch (Exception e) {
            System.err.println("❌ Failed to send cancellation email: " + e.getMessage());
        }

        return ResponseEntity.ok("✅ Booking cancelled successfully!");
    }
}
