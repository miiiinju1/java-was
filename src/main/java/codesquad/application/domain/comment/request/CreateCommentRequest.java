package codesquad.application.domain.comment.request;

public record CreateCommentRequest (
        Long postId,
        String content
) {
}
