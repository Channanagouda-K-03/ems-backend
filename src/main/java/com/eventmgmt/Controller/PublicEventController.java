package com.eventmgmt.Controller;

import com.eventmgmt.model.Event;
import com.eventmgmt.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class PublicEventController {

    @Autowired
    private EventRepository eventRepo;

    // ✅ Get all upcoming events
    @GetMapping("/all-events")
    public List<Event> getAllEvents() {
        return eventRepo.findAll(); // You can filter future events later
    }

    @GetMapping("/all")
    public Page<Event> getFilteredEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventDateTime") String sortBy,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return eventRepo.findFilteredEvents(location, keyword, after, pageable);
    }


    // ✅ Get event by ID
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }
}

