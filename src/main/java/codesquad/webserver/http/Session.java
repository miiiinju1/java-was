package codesquad.webserver.http;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {

    private final String sessionId;
    private final Long userId;
    private final LocalDateTime creationTime;
    private LocalDateTime lastAccessTime;
    private final long timeout;

    public Session(Long userId, LocalDateTime nowDateTime, long timeout) {
        this.sessionId = UUID.randomUUID().toString();
        this.userId = validateUserPk(userId);
        this.creationTime = validateNowDateTime(nowDateTime);
        this.lastAccessTime = this.creationTime;
        this.timeout = validateTimeout(timeout);
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public long getTimeout() {
        return timeout;
    }

    public void updateLastAccessTime() {
        this.lastAccessTime = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(lastAccessTime.plusSeconds(timeout));
    }

    private long validateUserPk(Long userPk) {
        if (userPk == null) {
            throw new IllegalArgumentException("사용자의 PK는 null일 수 없습니다.");
        }
        return userPk;
    }

    private LocalDateTime validateNowDateTime(LocalDateTime nowDateTime) {
        if (nowDateTime == null) {
            throw new IllegalArgumentException("현재 시간은 null일 수 없습니다.");
        }
        return nowDateTime;
    }

    private long validateTimeout(long timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout은 0보다 커야 합니다.");
        }
        return timeout;
    }
}
