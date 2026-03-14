package com.eventmgmt.Controller;

import com.eventmgmt.dto.FavoriteRequest;
import com.eventmgmt.exception.ResourceNotFoundException;
import com.eventmgmt.model.Event;
import com.eventmgmt.model.Favorite;
import com.eventmgmt.model.User;
import com.eventmgmt.repository.EventRepository;
import com.eventmgmt.repository.FavoriteRepository;
import com.eventmgmt.repository.UserRepository;
import com.eventmgmt.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@CrossOrigin(origins = "http://localhost:3000") // allow React dev origin
public class FavoriteController {

    private final FavoriteService favoriteService;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private FavoriteRepository favoriteRepository;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody FavoriteRequest request) {
        System.out.println("Received Favorite request: " + request); // helpful log
        Favorite saved = favoriteService.addFavorite(request.getUserId(), request.getEventId());
        return ResponseEntity.ok(saved.getId());
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@RequestParam Long userId, @RequestParam Long eventId) {
        favoriteService.removeFavorite(userId, eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> list(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.listByUser(userId));
    }
}
