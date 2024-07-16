package codesquad.application.mapper;

import codesquad.application.model.comment.Comment;
import codesquad.application.repository.vo.CommentVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentMapperTest {

    @DisplayName("Comment를 CommentVO로 변환")
    @Test
    void testToVo() {
        // given
        LocalDateTime now = LocalDateTime.of(2021, 6, 1, 0, 0, 0, 0);
        Comment comment = new Comment(1L, 2L, "This is comment!", now);
        comment.initCommentId(1L);

        // when
        CommentVO commentVO = CommentMapper.toCommentVO(comment);

        // then
        assertThat(commentVO).isNotNull()
                .extracting(CommentVO::commentId, CommentVO::postId, CommentVO::userId, CommentVO::content, CommentVO::createdDate)
                .contains(comment.getCommentId(), comment.getPostId(), comment.getUserId(), comment.getContent().getValue(), comment.getCreatedAt());
    }

    @DisplayName("CommentVO를 Comment로 변환")
    @Test
    void testToEntity() {
        // given
        LocalDateTime createdDate = LocalDateTime.of(2021, 6, 1, 0, 0, 0, 0);
        CommentVO commentVO = new CommentVO(1L, 2L, 2L, "This is a comment", createdDate);

        // when
        Comment comment = CommentMapper.toEntity(commentVO);

        // then
        assertThat(comment).isNotNull()
                .extracting(Comment::getPostId, Comment::getUserId, c -> c.getContent().getValue(), Comment::getCreatedAt)
                .contains(commentVO.postId(), commentVO.userId(), "This is a comment", createdDate);
    }
}
