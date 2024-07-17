package codesquad.application.domain.post.model;

import java.time.LocalDateTime;

public class Post {

    private Long postId;
    private final Long userId;
    private final Content content;
    private final ImagePath imagePath;
    private final LocalDateTime createdAt;

    public Long getPostId() {
        return postId;
    }
    public Long getUserId() {
        return userId;
    }

    public Content getContent() {
        return content;
    }

    public ImagePath getImagePath() {
        return imagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void initPostId(Long postId) {
        this.postId = postId;
    }

    public Post(
            Long userId,
            String content,
            String imagePath,
            LocalDateTime createdAt
    ) {
        validateUserId(userId);
        this.userId = userId;
        this.content = new Content(content);
        this.imagePath = new ImagePath(imagePath);
        this.createdAt = createdAt;
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 아이디가 없습니다.");
        }
    }

}
