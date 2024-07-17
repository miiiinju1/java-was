package codesquad.application.domain.post.response;

import codesquad.application.domain.comment.response.CommentListResponse;

import java.time.LocalDateTime;

public record PostResponse (
        Long postId,
        String nickname,
        String content,
        String imageName,
        CommentListResponse commentList,
        LocalDateTime createdAt
) {

}