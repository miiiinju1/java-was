package codesquad.application.mapper;

import codesquad.application.model.comment.Comment;
import codesquad.application.repository.vo.CommentVO;

public class CommentMapper {
    public static CommentVO toVo(Comment comment) {
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
