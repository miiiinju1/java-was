package codesquad.application.domain.comment.business;

import codesquad.application.config.H2TestDatabaseConfig;
import codesquad.application.database.dao.CommentDao;
import codesquad.application.database.dao.CommentDaoImpl;
import codesquad.application.database.dao.UserDao;
import codesquad.application.database.dao.UserDaoImpl;
import codesquad.application.database.vo.CommentVO;
import codesquad.application.domain.comment.request.CreateCommentRequest;
import codesquad.factory.TestUserVOFactory;
import codesquad.webserver.authorization.AuthorizationContext;
import codesquad.webserver.authorization.AuthorizationContextHolder;
import codesquad.webserver.http.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

class CreateCommentLogicTest {

    private final H2TestDatabaseConfig h2TestDatabaseConfig = new H2TestDatabaseConfig();
    private final UserDao userDao = new UserDaoImpl(h2TestDatabaseConfig);
    private final CommentDao commentDao = new CommentDaoImpl(h2TestDatabaseConfig);
    private final CreateCommentLogic createCommentLogic = new CreateCommentLogic(commentDao);

    @BeforeEach
    void setUp() {
        long 저장된_사용자_ID = userDao.save(TestUserVOFactory.createBy("userId1", "nickname1", "email1"));
        Session session = new Session(저장된_사용자_ID, LocalDateTime.of(2021, 1, 1, 0, 0), 60);
        AuthorizationContextHolder.setContext(new AuthorizationContext(session, List.of()));
    }

    @AfterEach
    void tearDown() {
        h2TestDatabaseConfig.resetDatabase();
        AuthorizationContextHolder.clearContext();
    }

    @DisplayName("run: 정상적으로 댓글을 생성하고 데이터베이스에 저장한다.")
    @Test
    void createCommentSuccess() {
        // given
        Long postId = 1L;
        CreateCommentRequest request = new CreateCommentRequest(postId, "This is a comment");

        // when
        createCommentLogic.run(request);

        // then
        List<CommentVO> byPostId = commentDao.findByPostId(1L);
        assertThat(byPostId).hasSize(1)
                .extracting("content", "postId", "userId")
                .contains(
                        tuple("This is a comment", postId, 1L)
                );

    }

    @DisplayName("run: 세션이 없는 상태에서 호출하면 예외를 던진다.")
    @Test
    void createCommentWithoutSession() {
        // given
        AuthorizationContextHolder.clearContext(); // 세션을 클리어하여 로그아웃 상태로 설정
        CreateCommentRequest request = new CreateCommentRequest(1L, "This is a comment");

        // when & then
        assertThatThrownBy(() -> createCommentLogic.run(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("로그인이 필요합니다.");
    }
}
