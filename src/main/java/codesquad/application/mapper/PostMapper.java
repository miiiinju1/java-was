package codesquad.application.mapper;

import codesquad.application.model.post.Post;
import codesquad.application.repository.vo.PostVO;

public class PostMapper {

    public static PostVO toPostVO(Post post) {
        return new PostVO(
                post.getPostId(),
                post.getUserId(),
                post.getContent().getValue(),
                post.getImagePath().getValue()
        );
    }

    public static Post toPost(PostVO postVO) {
        Post post = new Post(
                postVO.userId(),
                postVO.content(),
                postVO.imagePath()
        );
        post.initPostId(postVO.postId());
        return post;
    }
}
