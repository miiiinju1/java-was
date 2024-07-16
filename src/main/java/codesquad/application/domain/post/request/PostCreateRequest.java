package codesquad.application.domain.post.request;

public class PostCreateRequest {

    private final String content;
    private final String imageName;
    private final byte[] image;

    public PostCreateRequest(String content, String imageName, byte[] image) {
        this.content = content;
        this.imageName = imageName;
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public String getContent() {
        return content;
    }

    public byte[] getImage() {
        return image;
    }
}
