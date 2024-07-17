package codesquad.application.database.vo;

import java.time.LocalDateTime;

public record PostListVO (
        Long postId,
        Long userId,
        String nickname,
        String content,
        String imagePath,
        LocalDateTime createdAt
) {
}
