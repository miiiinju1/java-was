package codesquad.application.model.post;

import codesquad.application.model.comment.Comment;
import codesquad.application.model.comment.Comments;

import java.util.List;

public class Post {

    private Long postId;
    private final Content content;
    private final ImagePath imagePath;
    private final Comments comments;

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

    public Comments getComments() {
        return comments;
    }

    public Post(
            String content,
            String imagePath,
            List<Comment> comments
    ) {
        this.content = new Content(content);
        this.imagePath = new ImagePath(imagePath);
        this.comments = new Comments(comments);
    }

}
