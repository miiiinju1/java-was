package codesquad.application.database.dao;

import codesquad.application.config.H2TestDatabaseConfig;
import codesquad.application.database.vo.UserVO;
import codesquad.factory.TestUserVOFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserDaoImplTest {

    private final H2TestDatabaseConfig h2TestDatabaseConfig = new H2TestDatabaseConfig();
    private final UserDaoImpl userDao = new UserDaoImpl(h2TestDatabaseConfig);

    @AfterEach
    void tearDown() {
        h2TestDatabaseConfig.resetDatabase();
    }

    @DisplayName("saveUserVo: 정상적인 UserVo 저장")
    @Test
    void saveUserVo() {
        // given
        UserVO userVO = TestUserVOFactory.createDefaultUserVO();

        // when
        long save = userDao.save(userVO);

        // then
        assertAll(
                () -> assertThat(save).isEqualTo(1),
                () -> {
                    Optional<UserVO> maybeUserVO = userDao.findById(save);
                    assertAll(
                            () -> assertThat(maybeUserVO).isPresent()
                                    .get()
                                    .extracting("userId", "username", "password", "nickname", "email")
                                    .containsExactly(1L, userVO.username(), userVO.password(), userVO.nickname(), userVO.email()),
                            () -> assertThat(maybeUserVO.get().createdAt()).isNotNull()
                    );
                }
        );
    }

    @DisplayName("saveUserVo: UserVo가 null인 경우 예외 발생")
    @Test
    void saveUserVoWithNullUserVo() {
        // given
        UserVO userVO = null;

        // when & then
        assertThatThrownBy(() -> userDao.save(userVO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("UserVo는 null일 수 없습니다.");

    }

    @DisplayName("findById: 존재하는 UserVo 조회")
    @Test
    void findByIdWithExistentUserVo() {
        // given
        UserVO userVO1 = TestUserVOFactory.createBy("user1", "name1", "email1");
        UserVO userVO2 = TestUserVOFactory.createBy("user2", "name2", "email2");

        long savedId1 = userDao.save(userVO1);
        userDao.save(userVO2);

        // when
        Optional<UserVO> maybeUserVO = userDao.findById(savedId1);

        // then
        assertThat(maybeUserVO).isPresent()
                .get()
                .extracting("userId", "username", "password", "nickname", "email")
                .containsExactly(1L, userVO1.username(), userVO1.password(), userVO1.nickname(), userVO1.email());
    }

    @DisplayName("findById: 존재하지 않는 UserVo 조회 시 Optional에 null이 들어간 값 반환")
    @Test
    void findByIdWithNonExistentUserVo() {
        // given
        long 존재하지_않는_id = 2L;

        // when
        Optional<UserVO> maybeUserVo = userDao.findById(존재하지_않는_id);

        // then
        assertThat(maybeUserVo).isEmpty();
    }

    @DisplayName("findAll: 저장된 모든 UserVo 조회")
    @Test
    void findAll() {
        // given
        UserVO userVO1 = TestUserVOFactory.createBy("user1", "name1", "email1");
        UserVO userVO2 = TestUserVOFactory.createBy("user2", "name2", "email2");

        userDao.save(userVO1);
        userDao.save(userVO2);

        // when
        List<UserVO> allUserVOs = userDao.findAll();

        // then
        assertThat(allUserVOs).hasSize(2)
                .extracting("userId", "username", "password", "nickname", "email")
                .containsExactly(
                        tuple(1L, userVO1.username(), userVO1.password(), userVO1.nickname(), userVO1.email()),
                        tuple(2L, userVO2.username(), userVO2.password(), userVO2.nickname(), userVO2.email())
                );
    }

    @DisplayName("update: 정상적인 UserVo 수정")
    @Test
    void update() {
        // given
        UserVO 기존_VO = TestUserVOFactory.createDefaultUserVO();
        long savedId = userDao.save(기존_VO);

        UserVO 업데이트_하려는_VO = new UserVO(
                savedId,
                "updatedUsername",
                "updatedPassword",
                "updatedName",
                "updatedEmail",
                null
        );

        // when
        userDao.update(savedId, 업데이트_하려는_VO);

        // then
        Optional<UserVO> maybeUserVO = userDao.findById(savedId);
        assertAll(
                () -> assertThat(maybeUserVO).isPresent()
                        .get()
                        .extracting("userId", "username", "password", "nickname", "email")
                        .containsExactly(savedId, 업데이트_하려는_VO.username(), 업데이트_하려는_VO.password(), 업데이트_하려는_VO.nickname(), 업데이트_하려는_VO.email()),
                () -> assertThat(maybeUserVO.get().createdAt()).isNotNull()
        );
    }

    @DisplayName("update: 존재하지 않는 UserVo 수정 시 예외 발생")
    @Test
    void updateWithNonExistentUserVo() {
        // given
        long 업데이트_하려는_ID = 1L;
        UserVO 업데이트_하려는_VO = new UserVO(
                업데이트_하려는_ID,
                "updatedUsername",
                "updatedPassword",
                "updatedName",
                "updatedEmail",
                null
        );

        // when & then
        assertThatThrownBy(() -> userDao.update(업데이트_하려는_ID, 업데이트_하려는_VO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("존재하지 않는 User입니다.");
    }

    @DisplayName("delete: 정상적인 UserVo 삭제")
    @Test
    void delete() {
        // given
        UserVO userVO = TestUserVOFactory.createDefaultUserVO();
        long savedId = userDao.save(userVO);

        // when
        userDao.delete(savedId);

        // then
        assertThat(userDao.findById(savedId)).isEmpty();
    }

    @DisplayName("delete: 존재하지 않는 UserVo 삭제 시 예외 발생")
    @Test
    void deleteWithNonExistentUserVo() {
        // given
        long 삭제_하려는_ID = 1L;

        // when & then
        assertThatThrownBy(() -> userDao.delete(삭제_하려는_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("존재하지 않는 User입니다.");
    }
    
    @DisplayName("findByUsername: 존재하는 username으로 UserVo 조회")
    @Test
    void findByUsernameWithExistentUsername() {
        // given
        UserVO userVO = TestUserVOFactory.createDefaultUserVO();
        userDao.save(userVO);

        // when
        Optional<UserVO> maybeUserVO = userDao.findByUsername(userVO.username());

        // then
        assertThat(maybeUserVO).isPresent()
                .get()
                .extracting("userId", "username", "password", "nickname", "email")
                .containsExactly(1L, userVO.username(), userVO.password(), userVO.nickname(), userVO.email());
    }
    
    @DisplayName("findByUsername: 존재하지 않는 username으로 UserVo 조회 시 Optional에 null이 들어간 값 반환")
    @Test
    void findByUsernameWithNonExistentUsername() {
        // given
        String 존재하지_않는_username = "존재하지_않는_username";

        // when
        Optional<UserVO> maybeUserVO = userDao.findByUsername(존재하지_않는_username);

        // then
        assertThat(maybeUserVO).isEmpty();
    }

    @DisplayName("findByUsername: username이 null인 경우 예외 발생")
    @Test
    void findByUsernameWithNullUsername() {
        // given
        String nullUsername = null;

        // when & then
        assertThatThrownBy(() -> userDao.findByUsername(nullUsername))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("username은 null이거나 빈 문자열일 수 없습니다.");
    }
}