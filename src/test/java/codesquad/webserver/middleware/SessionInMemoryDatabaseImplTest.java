package codesquad.webserver.middleware;

import codesquad.webserver.http.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class SessionInMemoryDatabaseImplTest {

    private Long validUserPk;
    private String sessionKey;

    @BeforeEach
    void setUp() {
        validUserPk = 1L;
        SessionDatabase.clear();
    }

    @DisplayName("save: 정상적인 세션 저장")
    @Test
    void testSave() {
        // given
        validUserPk = 1L;

        // when
        Session session = SessionDatabase.save(validUserPk);

        // then
        assertThat(session)
                .isNotNull()
                .extracting(Session::getUserPk, Session::getCreationTime, Session::getLastAccessTime, Session::getTimeout)
                .doesNotContainNull()
                .containsExactly(validUserPk, session.getCreationTime(), session.getLastAccessTime(), 3600L);
    }

    @DisplayName("save: userPk가 null인 경우 예외 발생")
    @Test
    void testSaveWithNullUserPk() {
        // given
        Long nullUserPk = null;

        // when & then
        assertThatThrownBy(() -> SessionDatabase.save(nullUserPk))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자의 PK는 null일 수 없습니다.");
    }

    @DisplayName("save: 동일한 userPk로 여러 세션 저장")
    @Test
    void testSaveMultipleSessionsForSameUserPk() {
        // given
        validUserPk = 1L;

        // when
        Session session1 = SessionDatabase.save(validUserPk);
        Session session2 = SessionDatabase.save(validUserPk);

        // then
        assertThat(session1.getSessionId())
                .isNotEqualTo(session2.getSessionId());
    }

    @DisplayName("find: 정상적인 세션 조회")
    @Test
    void testFind() {
        // given
        Session session = SessionDatabase.save(validUserPk);

        System.out.println("session.getSessionId() = " + session.getSessionId());
        // when
        Session foundSession = SessionDatabase.find(session.getSessionId());


        // then
        assertThat(foundSession)
                .isNotNull()
                .extracting(Session::getUserPk)
                .isEqualTo(validUserPk);
    }

    @DisplayName("find: 존재하지 않는 키로 조회 시 null 반환")
    @Test
    void testFindNonExistentKey() {
        // given
        sessionKey = UUID.randomUUID().toString();

        // when
        Session foundSession = SessionDatabase.find(sessionKey);

        // then
        assertThat(foundSession).isNull();
    }

    @DisplayName("find: 저장 후 조회")
    @Test
    void testFindAfterSave() {
        // given
        Session session = SessionDatabase.save(validUserPk);

        // when
        Session foundSession = SessionDatabase.find(session.getSessionId());

        // then
        assertThat(foundSession).isEqualTo(session);
    }

    @DisplayName("delete: 정상적인 세션 삭제")
    @Test
    void testDelete() {
        // given
        Session session = SessionDatabase.save(validUserPk);

        // when
        SessionDatabase.delete(session.getSessionId());

        // then
        assertThat(SessionDatabase.find(session.getSessionId())).isNull();
    }

    @DisplayName("delete: 존재하지 않는 키로 삭제 시 예외 발생 없음")
    @Test
    void testDeleteNonExistentKey() {
        // given
        sessionKey = UUID.randomUUID().toString();

        // when & then
        assertThatCode(() -> SessionDatabase.delete(sessionKey)).doesNotThrowAnyException();
    }

    @DisplayName("delete: 저장 후 삭제")
    @Test
    void testDeleteAfterSave() {
        // given
        Session session = SessionDatabase.save(validUserPk);

        // when
        SessionDatabase.delete(session.getSessionId());

        // then
        assertThat(SessionDatabase.find(session.getSessionId())).isNull();
    }

    @DisplayName("containsKey: 저장된 세션 키 확인")
    @Test
    void testContainsKey() {
        // given
        Session session = SessionDatabase.save(validUserPk);

        // when
        boolean contains = SessionDatabase.containsKey(session.getSessionId());

        // then
        assertThat(contains).isTrue();
    }

    @DisplayName("containsKey: 존재하지 않는 키 확인")
    @Test
    void testContainsNonExistentKey() {
        // given
        sessionKey = UUID.randomUUID().toString();

        // when
        boolean contains = SessionDatabase.containsKey(sessionKey);

        // then
        assertThat(contains).isFalse();
    }

    @DisplayName("containsKey: 삭제 후 키 확인")
    @Test
    void testContainsKeyAfterDelete() {
        // given
        Session session = SessionDatabase.save(validUserPk);

        // when
        SessionDatabase.delete(session.getSessionId());

        // then
        boolean contains = SessionDatabase.containsKey(session.getSessionId());
        assertThat(contains).isFalse();
    }
}
