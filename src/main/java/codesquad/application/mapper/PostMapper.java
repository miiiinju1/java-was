package codesquad.application.mapper;

import codesquad.application.domain.post.model.Post;
import codesquad.application.database.vo.PostVO;

public class PostMapper {

    public static PostVO toPostVO(Post post) {
        return new PostVO(
                post.getPostId(),
                post.getUserId(),
                post.getContent().getValue(),
                post.getImagePath().getValue(),
                post.getCreatedAt()
        );
    }

    public static Post toPost(PostVO postVO) {
        Post post = new Post(
                postVO.userId(),
                postVO.content(),
                postVO.imagePath(),
                postVO.createdAt()
        );
        post.initPostId(postVO.postId());
        return post;
    }
}
