package codesquad.database;

import codesquad.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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


    @Test
    @Disabled("동기화 문제를 확인하기 위한 테스트")
    void testConcurrentDuplicateUserId() throws InterruptedException {
        final int THREAD_COUNT = 10;
        UserDatabase userDatabase = new UserDatabase();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);
        AtomicInteger duplicateCount = new AtomicInteger(0);
        String duplicateUserId = "duplicateUser";

        int loopCount = THREAD_COUNT * 10;

        for (int i = 0; i < loopCount; i++) {
            executorService.submit(() -> {
                try {
                    barrier.await();
                    User user = new User(duplicateUserId, "password", "name", "email");
                    userDatabase.save(user);

                } catch (IllegalArgumentException e) {
                    duplicateCount.incrementAndGet();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // 중복된 userId로 저장된 사용자 수는 1이어야 함
        assertEquals(1, userDatabase.findAll().size());

        // 중복된 userId로 저장된 사용자 수는 loopCount - 1이어야 함
        assertEquals(loopCount - 1, duplicateCount.get());
    }

}