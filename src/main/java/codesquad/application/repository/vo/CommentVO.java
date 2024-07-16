package codesquad.application.repository.vo;

import java.time.LocalDateTime;

public record CommentVO (
        Long commentId,
        Long postId,
        Long userId,
        String content,
        LocalDateTime createdDate
) {
}
