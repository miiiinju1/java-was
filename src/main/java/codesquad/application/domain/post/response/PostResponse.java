package codesquad.application.domain.post.response;

import codesquad.application.domain.comment.response.CommentListResponse;

public record PostResponse (
        Long postId,
        String nickname,
        String content,
        String imageName,
        CommentListResponse commentList
) {

}