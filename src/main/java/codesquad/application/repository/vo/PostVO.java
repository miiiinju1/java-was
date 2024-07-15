package codesquad.application.repository.vo;

public record PostVO(
        Long postId,
        Long userId,
        String content,
        String imagePath
) {
}
