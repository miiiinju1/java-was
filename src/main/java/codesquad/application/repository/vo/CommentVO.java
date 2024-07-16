package codesquad.application.repository.vo;

import java.time.LocalDateTime;

public record CommentVO (
        long commentId,
        long postId,
        long userId,
        String content,
        LocalDateTime createdDate
) {
}
