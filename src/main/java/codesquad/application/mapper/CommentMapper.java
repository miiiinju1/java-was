package codesquad.application.mapper;

import codesquad.application.domain.comment.model.Comment;
import codesquad.application.database.CommentVO;

public class CommentMapper {
    public static CommentVO toCommentVO(Comment comment) {
        return new CommentVO(
                comment.getCommentId(),
                comment.getPostId(),
                comment.getUserId(),
                comment.getContent().getValue(),
                comment.getCreatedAt()
        );
    }

    public static Comment toEntity(CommentVO commentVO) {
        return new Comment(
                commentVO.postId(),
                commentVO.userId(),
                commentVO.content(),
                commentVO.createdDate()
        );
    }

    private CommentMapper() {
    }
}
