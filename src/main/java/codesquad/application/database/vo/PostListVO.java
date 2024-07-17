package codesquad.application.database.vo;

public record PostListVO (
        Long postId,
        Long userId,
        String nickname,
        String content,
        String imagePath
) {
}
