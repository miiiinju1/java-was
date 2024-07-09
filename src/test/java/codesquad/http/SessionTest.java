package codesquad.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionTest {
    
    private Long validUserPk;
    private LocalDateTime nowDateTime;
    private long validTimeout;

    @BeforeEach
    void setUp() {
        validUserPk = 1L;
        nowDateTime = LocalDateTime.of(2021, 6, 1, 0, 0, 0);
        validTimeout = 1800L;
    }

    @DisplayName("Session 생성 시 유효한 값으로 생성되는지 테스트")
    @Test
    void testSessionCreation() {
        Session session = new Session(validUserPk, nowDateTime, validTimeout);

        assertThat(session)
                .isNotNull()
                .extracting(Session::getSessionId, Session::getUserPk, Session::getCreationTime, Session::getLastAccessTime, Session::getTimeout)
                .doesNotContainNull()
                .containsExactly(session.getSessionId(), validUserPk, nowDateTime, nowDateTime, validTimeout);
    }

    @DisplayName("Session 생성 시 userPk가 null이면 예외 발생")
    @Test
    void testSessionCreationWithNullUserPk() {
        assertThatThrownBy(() -> new Session(null, nowDateTime, validTimeout))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자의 PK는 null일 수 없습니다.");
    }

    @DisplayName("Session 생성 시 nowDateTime이 null이면 예외 발생")
    @Test
    void testSessionCreationWithNullNowDateTime() {
        assertThatThrownBy(() -> new Session(validUserPk, null, validTimeout))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 시간은 null일 수 없습니다.");
    }

    @DisplayName("Session 생성 시 timeout이 0보다 작으면 예외 발생")
    @Test
    void testSessionCreationWithInvalidTimeout() {
        assertThatThrownBy(() -> new Session(validUserPk, nowDateTime, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("timeout은 0보다 커야 합니다.");
    }

    @DisplayName("updateLastAccessTime 메서드가 lastAccessTime을 현재 시간으로 업데이트하는지 테스트")
    @Test
    void testUpdateLastAccessTime() {
        Session session = new Session(validUserPk, nowDateTime, validTimeout);
        LocalDateTime beforeUpdate = session.getLastAccessTime();

        session.updateLastAccessTime();
        LocalDateTime afterUpdate = session.getLastAccessTime();

        assertThat(afterUpdate).isAfter(beforeUpdate);
    }

    @DisplayName("isExpired 메서드가 세션의 만료 여부를 올바르게 반환하는지 테스트")
    @Test
    void testIsExpired() {
        Session session = new Session(validUserPk, nowDateTime.minusSeconds(2), 1);

        assertThat(session.isExpired())
                .isTrue();
    }
}