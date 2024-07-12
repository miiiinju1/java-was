package codesquad.database;

import codesquad.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDatabaseTest {

    @DisplayName("userDatabase에 save할 때 userID가 이미 존재하면 IllegalArgumentException을 던진다.")
    @Test
    void putIfAbsentWhenKeyExists() {
        // given
        UserDatabase userDatabase = new UserDatabase();

        User user1 = new User("userId", "password", "name", "email");
        User user2 = new User("userId", "pasdf", "name2", "email2");

        userDatabase.save(user1);


        // when & then
        assertThatThrownBy(() -> userDatabase.save(user2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 userId 입니다.");
    }

    @DisplayName("userDatabase에 save할 때 userID가 없으면 저장에 성공한다.")
    @Test
    void putIfAbsentWhenKeyDoesNotExists() {
        // given
        UserDatabase userDatabase = new UserDatabase();

        User user1 = new User("userId", "password", "name", "email");

        // when
        long save = userDatabase.save(user1);

        // then
        assertEquals(1, save);
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