package codesquad.application.domain.comment.model;

import java.util.List;

public class Comments {
    private final List<Comment> values;

    public List<Comment> getValues() {
        return values;
    }

    public Comments(List<Comment> comments) {
        if (comments == null) {
            throw new IllegalArgumentException("댓글 목록이 없습니다.");
        }
        this.values = comments;
    }
}
