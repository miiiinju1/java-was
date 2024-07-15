package codesquad.application.model.post;

public class Post {

    private Long postId;
    private final Long userId;
    private final Content content;
    private final ImagePath imagePath;

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

    public void initPostId(Long postId) {
        this.postId = postId;
    }

    public Post(
            Long userId,
            String content,
            String imagePath
    ) {
        this.userId = userId;
        this.content = new Content(content);
        this.imagePath = new ImagePath(imagePath);
    }

}
