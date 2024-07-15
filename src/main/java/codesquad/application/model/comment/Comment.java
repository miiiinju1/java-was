package codesquad.application.model.comment;

import java.time.LocalDateTime;

public class Comment {

    private Long commentId;
    private final Long postId;
    private final Long userId;
    private final String content;
    private final LocalDateTime createdAt;

    public void initCommentId(Long commentId) {
        this.commentId = commentId;
    }
    public Long getCommentId() {
        return commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Comment(Long postId, Long userId, String content, LocalDateTime createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
