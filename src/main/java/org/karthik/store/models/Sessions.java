package org.karthik.store.models;

public class Sessions {
    private String sessionId;
    private Integer userId;
    private Long createdAt;
    private Long lastAccessedAt;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {

         this.userId = userId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    public Long getLastAccessedAt() {
        return lastAccessedAt;
    }
    public void setLastAccessedAt(Long lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    @Override
    public String toString() {
        return "Sessions{" +
                "sessionId='" + sessionId + '\'' +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", lastAccessedAt=" + lastAccessedAt +
                '}';
    }
}
