package codesquad.application.model.post;

public class Post {

    private Long postId;
    private final Content content;
    private final ImagePath imagePath;

    public Long getPostId() {
        return postId;
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
            String content,
            String imagePath
    ) {
        this.content = new Content(content);
        this.imagePath = new ImagePath(imagePath);
    }

}
