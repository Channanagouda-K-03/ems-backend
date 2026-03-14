package com.eventmgmt.Controller;

import com.eventmgmt.model.Event;
import com.eventmgmt.model.OurService;
import com.eventmgmt.repository.EventRepository;
import com.eventmgmt.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "http://localhost:3000")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepo;

    @GetMapping("/all")
    public ResponseEntity<List<OurService>> getAllServices() {
        return ResponseEntity.ok(serviceRepo.findAll());
    }

    @PostMapping("/book/{serviceId}")
    public ResponseEntity<String> bookService(@PathVariable Long serviceId) {
        return ResponseEntity.ok("Service with ID " + serviceId + " booked successfully!");
    }
}
