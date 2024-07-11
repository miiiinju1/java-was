package codesquad.database;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class UserDatabaseTest {

    @DisplayName("putIfAbsent는 key가 존재하지 않을 때 value를 추가하고 null을 반환한다.")
    @Test
    void putIfAbsentWhenKeyDoesNotExist() {
        // given
        ConcurrentHashMap<String, String> userIdUnique = new ConcurrentHashMap<>();

        String a = "test";
        String a_ = "value";

        // when
        String s1 = userIdUnique.putIfAbsent(a, a_);

        // then
        assertThat(s1).isNull();
    }

    @DisplayName("putIfAbsent는 key가 이미 존재하면 기존 value를 반환한다.")
    @Test
    void putIfAbsentWhenKeyExists() {
        // given
        ConcurrentHashMap<String, String> userIdUnique = new ConcurrentHashMap<>();

        String a = "test";
        String b = "test";
        String a_ = "value";
        String b_ = "value";
        userIdUnique.putIfAbsent(a, a_);

        // when
        String s2 = userIdUnique.putIfAbsent(b, b_);

        // then
        assertThat(s2).isEqualTo(a_);
    }

}