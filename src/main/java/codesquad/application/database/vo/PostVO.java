package codesquad.application.database.vo;

import java.time.LocalDateTime;

public record PostVO(
        Long postId,
        Long userId,
        String content,
        String imagePath,
        LocalDateTime createdAt
) {
}
