package codesquad.application.repository.vo;

public record PostListVO (
        Long postId,
        Long userId,
        String nickname,
        String content,
        String imagePath
) {
}
