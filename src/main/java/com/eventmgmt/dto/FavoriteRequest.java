package com.eventmgmt.dto;

public class FavoriteRequest {
    private Long userId;
    private Long eventId;
    public FavoriteRequest() {}
    public FavoriteRequest(Long id, Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    @Override
    public String toString() {
        return "FavoriteRequest{userId=" + userId + ", eventId=" + eventId + "}";
    }
}
