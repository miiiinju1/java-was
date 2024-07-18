package codesquad.application.domain.post.business;

import codesquad.application.config.H2TestDatabaseConfig;
import codesquad.application.database.dao.PostDao;
import codesquad.application.database.dao.PostDaoImpl;
import codesquad.application.database.dao.UserDao;
import codesquad.application.database.dao.UserDaoImpl;
import codesquad.application.database.vo.PostVO;
import codesquad.application.domain.post.request.PostCreateRequest;
import codesquad.factory.TestUserVOFactory;
import codesquad.webserver.authorization.AuthorizationContext;
import codesquad.webserver.authorization.AuthorizationContextHolder;
import codesquad.webserver.helper.FileSaveHelper;
import codesquad.webserver.http.Session;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostCreateLogicTest {

    private final H2TestDatabaseConfig h2TestDatabaseConfig = new H2TestDatabaseConfig();
    private final UserDao userDao = new UserDaoImpl(h2TestDatabaseConfig);
    private final PostDao postDao = new PostDaoImpl(h2TestDatabaseConfig);

    private PostCreateLogic postCreateLogic;

    @BeforeEach
    void setUp() {
        postCreateLogic = new PostCreateLogic(userDao, postDao);
        long 저장된_사용자_ID = userDao.save(TestUserVOFactory.createBy("userId1", "nickname1", "email1"));
        Session session = new Session(저장된_사용자_ID, LocalDateTime.of(2021, 1, 1, 0, 0), 60);
        AuthorizationContextHolder.setContext(new AuthorizationContext(session, List.of()));
    }

    @AfterEach
    void tearDown() {
        h2TestDatabaseConfig.resetDatabase();
        AuthorizationContextHolder.clearContext();

    }

    @DisplayName("run: 정상적으로 포스트를 생성하고 데이터베이스에서 확인한다.")
    @Test
    @Disabled("pk를 고정할 수 없음")
    void createPostSuccess() {
        // given
        PostCreateRequest request = new PostCreateRequest("content", "imageName", new byte[0]);

        // when
        postCreateLogic.run(request);

        // then
        Optional<PostVO> maybePostVO = postDao.findById(1L);// Assuming the post ID starts from 1

        assertThat(maybePostVO).isPresent()
                        .get()
                .extracting("userId", "content")
                .containsExactly(1L, "content");

        String imagePath = maybePostVO.get().imagePath();
        assertThat(imagePath).isNotNull();
        assertThat(FileSaveHelper.removeFile(imagePath)).isTrue();
    }

    @DisplayName("run: 로그인 하지 않은 상태에서 호출하면 예외를 던진다.")
    @Test
    void createPostWithoutLogin() {
        // given
        AuthorizationContextHolder.clearContext(); // 로그아웃 상태로 변경
        PostCreateRequest request = new PostCreateRequest("content", "imageName", new byte[0]);

        // when & then
        assertThatThrownBy(() -> postCreateLogic.run(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("로그인이 필요합니다.");
    }

    @DisplayName("run: 파일 저장에 실패하면 예외를 던진다.")
    @Disabled("FilesaveHelper가 static이라 테스트가 어려움")
    @Test
    void createPostFileSaveFailure() {
        // given
        PostCreateRequest request = new PostCreateRequest("content", "imageName", new byte[0]);

        // when & then
        assertThatThrownBy(() -> postCreateLogic.run(request))
                .isInstanceOf(RuntimeException.class);
    }
}
