package codesquad.application.domain.post.response;

public record PostResponse (
        Long postId,
        String nickname,
        String content,
        String imageName
) {

}