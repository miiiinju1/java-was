package codesquad.application.database.dao;

import codesquad.application.config.CSVTestDatabaseConfig;
import codesquad.application.database.vo.CommentListVO;
import codesquad.application.database.vo.CommentVO;
import codesquad.application.database.vo.PostVO;
import codesquad.application.database.vo.UserVO;
import codesquad.application.helper.Base64Util;
import codesquad.csvdb.jdbc.CsvExecutor;
import codesquad.factory.TestPostVOFacotry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class CSVCommentDaoImplTest {

    private final CSVTestDatabaseConfig csvTestDatabaseConfig = new CSVTestDatabaseConfig();
    private final CommentDaoImpl commentDao = new CommentDaoImpl(csvTestDatabaseConfig);
    private final PostDao postDao = new PostDaoImpl(csvTestDatabaseConfig);

    @BeforeEach
    void setUp() {
        CsvExecutor.clear();
    }

    @AfterEach
    void tearDown() {
        csvTestDatabaseConfig.resetDatabase();
    }

    @DisplayName("save: 정상적인 CommentVO 저장")
    @Test
    void saveCommentVO() {
        // given
        CommentVO commentVO = new CommentVO(null, 1L, 1L, "content", LocalDateTime.now());

        // when
        long commentId = commentDao.save(commentVO);

        // then
        Optional<CommentVO> savedCommentVO = commentDao.findById(commentId);
        assertThat(savedCommentVO).isPresent()
                .get()
                .extracting("postId", "userId", "content")
                .containsExactly(1L, 1L, "content");
    }

    @DisplayName("findById: 존재하는 CommentVO 조회")
    @Test
    void findByIdWithExistentCommentVO() {
        // given
        CommentVO commentVO = new CommentVO(null, 1L, 1L, "content", LocalDateTime.now());
        long commentId = commentDao.save(commentVO);

        // when
        Optional<CommentVO> foundCommentVO = commentDao.findById(commentId);

        // then
        assertThat(foundCommentVO).isPresent()
                .get()
                .extracting("postId", "userId", "content")
                .containsExactly(1L, 1L, "content");
    }

    @DisplayName("findById: 존재하지 않는 CommentVO 조회")
    @Test
    void findByIdWithNonExistentCommentVO() {
        // given
        Long nonExistentCommentId = 999L;

        // when
        Optional<CommentVO> foundCommentVO = commentDao.findById(nonExistentCommentId);

        // then
        assertThat(foundCommentVO).isEmpty();
    }

    @DisplayName("findAll: 저장된 모든 CommentVO 조회")
    @Test
    void findAllComments() {
        // given
        CommentVO commentVO1 = new CommentVO(null, 1L, 1L, "content1", LocalDateTime.now());
        CommentVO commentVO2 = new CommentVO(null, 1L, 2L, "content2", LocalDateTime.now());
        commentDao.save(commentVO1);
        commentDao.save(commentVO2);

        // when
        Collection<CommentVO> allComments = commentDao.findAll();

        // then
        assertThat(allComments).hasSize(2)
                .extracting("postId", "userId", "content")
                .containsExactlyInAnyOrder(
                        tuple(1L, 1L, "content1"),
                        tuple(1L, 2L, "content2")
                );
    }

    @DisplayName("findCommentsJoinFetch: postId에 따른 CommentListVO 조회")
    @Test
    void findCommentsJoinFetch() {
        // given
        UserDao userDao = new UserDaoImpl(csvTestDatabaseConfig);
        UserVO userVO = new UserVO(null, "user1", "password1", "user1", "email1", null);
        long userId = userDao.save(userVO);
        PostVO postVO = TestPostVOFacotry.createDefaultPostVO(userId);
        long postId = postDao.save(postVO);
        CommentVO postVO1 = new CommentVO(null, postId, userId, "Comment 1", null);
        CommentVO postVO2 = new CommentVO(null, postId, userId, "Comment 2", null);

        long commentId1 = commentDao.save(postVO1);
        long commentId2 = commentDao.save(postVO2);

        // when
        List<CommentListVO> comments = commentDao.findCommentsJoinFetch(postId);

        // then
        assertThat(comments).hasSize(2)
                .extracting("commentId", "postId", "userId", "nickname", "content")
                .containsExactlyInAnyOrder(
                        tuple(commentId1, postId, userId, "user1", "Comment 1"),
                        tuple(commentId2, postId, userId, "user1", "Comment 2")
                );

    }

}
