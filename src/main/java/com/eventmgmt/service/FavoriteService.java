package com.eventmgmt.service;

import com.eventmgmt.model.Event;
import com.eventmgmt.model.Favorite;
import com.eventmgmt.model.User;
import com.eventmgmt.repository.EventRepository;
import com.eventmgmt.repository.FavoriteRepository;
import com.eventmgmt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           UserRepository userRepository,
                           EventRepository eventRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Favorite addFavorite(Long userId, Long eventId) {
        if (userId == null || eventId == null) {
            throw new IllegalArgumentException("userId and eventId are required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + eventId));

        if (favoriteRepository.existsByUser_UserIdAndEvent_EventId(userId, eventId)) {
            // idempotent behavior: return existing rather than error
            return favoriteRepository.findByUser_UserIdAndEvent_EventId(userId, eventId).get();
        }

        Favorite favorite = new Favorite(user, event);
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(Long userId, Long eventId) {
        if (!favoriteRepository.existsByUser_UserIdAndEvent_EventId(userId, eventId)) return;
        favoriteRepository.deleteByUser_UserIdAndEvent_EventId(userId, eventId);
    }

    @Transactional(readOnly = true)
    public List<Favorite> listByUser(Long userId) {
        return favoriteRepository.findByUser_UserId(userId);
    }
}
