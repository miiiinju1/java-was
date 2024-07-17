package codesquad.application.domain.comment.response;

import java.time.LocalDateTime;

public record CommentResponse (
        Long commentId,
        String nickname,
        String content,
        LocalDateTime createdAt
) {
}
