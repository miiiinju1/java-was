package codesquad.application.database.dao;

import codesquad.application.config.CSVTestDatabaseConfig;
import codesquad.application.database.vo.UserVO;
import codesquad.csvdb.jdbc.CsvExecutor;
import codesquad.factory.TestUserVOFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

class CSVUserDaoImplTest {

    private final CSVTestDatabaseConfig csvTestDatabaseConfig = new CSVTestDatabaseConfig();
    private final UserDaoImpl userDao = new UserDaoImpl(csvTestDatabaseConfig);


    @BeforeEach
    void setUp() {
        CsvExecutor.clear();
    }

    @AfterEach
    void tearDown() {
        csvTestDatabaseConfig.resetDatabase();
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
                () -> {
                    Optional<UserVO> maybeUserVO = userDao.findById(save);
                    assertAll(
                            () -> assertThat(maybeUserVO).isPresent()
                                    .get()
                                    .extracting("username", "password", "email", "nickname")
                                    .containsExactly(userVO.username(), userVO.password(), userVO.email(), userVO.nickname()),
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
                .extracting("username", "password", "nickname", "email")
                .contains(userVO1.username(), userVO1.password(), userVO1.nickname(), userVO1.email());
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
                .extracting( "username", "password", "nickname", "email")
                .contains(
                        tuple( userVO1.username(), userVO1.password(), userVO1.nickname(), userVO1.email()),
                        tuple(userVO2.username(), userVO2.password(), userVO2.nickname(), userVO2.email())
                );
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
                .extracting("username", "password", "nickname", "email")
                .containsExactly(userVO.username(), userVO.password(), userVO.nickname(), userVO.email());
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