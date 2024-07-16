package codesquad.application.database.dao;

import codesquad.application.config.H2TestDatabaseConfig;
import codesquad.application.repository.vo.CommentVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

class CommentDaoImplTest {

    private final H2TestDatabaseConfig h2TestDatabaseConfig = new H2TestDatabaseConfig();
    private final CommentDaoImpl commentDao = new CommentDaoImpl(h2TestDatabaseConfig);

    @AfterEach
    void tearDown() {
        h2TestDatabaseConfig.resetDatabase();
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

    @DisplayName("delete: 정상적인 CommentVO 삭제")
    @Test
    void deleteCommentVO() {
        // given
        CommentVO commentVO = new CommentVO(null, 1L, 1L, "content", LocalDateTime.now());
        long commentId = commentDao.save(commentVO);

        // when
        commentDao.delete(commentId);

        // then
        Optional<CommentVO> deletedCommentVO = commentDao.findById(commentId);
        assertThat(deletedCommentVO).isEmpty();
    }

    @DisplayName("delete: 존재하지 않는 CommentVO 삭제 시 예외 발생")
    @Test
    void deleteNonExistentCommentVO() {
        // given
        Long nonExistentCommentId = 999L;

        // when & then
        assertThatThrownBy(() -> commentDao.delete(nonExistentCommentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("comment를 삭제하는 데 실패했습니다, no rows affected.");
    }

    @DisplayName("update: 정상적인 CommentVO 수정")
    @Test
    void updateCommentVO() {
        // given
        CommentVO commentVO = new CommentVO(null, 1L, 1L, "content", LocalDateTime.now());
        long commentId = commentDao.save(commentVO);

        CommentVO updatedCommentVO = new CommentVO(commentId, 1L, 1L, "updated content", LocalDateTime.now());

        // when
        commentDao.update(commentId, updatedCommentVO);

        // then
        Optional<CommentVO> foundCommentVO = commentDao.findById(commentId);
        assertThat(foundCommentVO).isPresent()
                .get()
                .extracting("postId", "userId", "content")
                .containsExactly(1L, 1L, "updated content");
    }

    @DisplayName("update: 존재하지 않는 CommentVO 수정 시 예외 발생")
    @Test
    void updateNonExistentCommentVO() {
        // given
        CommentVO updatedCommentVO = new CommentVO(999L, 1L, 1L, "updated content", LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> commentDao.update(999L, updatedCommentVO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("comment를 업데이트하는 데 실패했습니다, no rows affected.");
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

    @DisplayName("save: content가 null인 경우 예외 발생")
    @Test
    void saveCommentVOWithNullContent() {
        // given
        CommentVO commentVO = new CommentVO(null, 1L, 1L, null, LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> commentDao.save(commentVO))
                .isInstanceOf(RuntimeException.class);
    }
}
