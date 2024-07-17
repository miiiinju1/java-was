package codesquad.application.mapper;

import codesquad.application.domain.post.model.Post;
import codesquad.application.database.vo.PostVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostMapperTest {

    @DisplayName("Post를 PostVO로 변환")
    @Test
    void testToPostVO() {
        // given
        Post post = new Post(1L, "This is a post", "image.png", null);
        post.initPostId(1L);

        // when
        PostVO postVO = PostMapper.toPostVO(post);

        // then
        assertThat(postVO).isNotNull()
                .extracting(PostVO::postId, PostVO::userId, PostVO::content, PostVO::imagePath)
                .contains(post.getPostId(), post.getUserId(), post.getContent().getValue(), post.getImagePath().getValue());
    }

    @DisplayName("PostVO를 Post로 변환")
    @Test
    void testToPost() {
        // given
        PostVO postVO = new PostVO(1L, 1L, "This is a post", "image.png", null);

        // when
        Post post = PostMapper.toPost(postVO);

        // then
        assertThat(post).isNotNull()
                        .extracting(Post::getPostId, Post::getUserId, p -> p.getContent().getValue(), p -> p.getImagePath().getValue())
                        .contains(postVO.postId(), postVO.userId(), "This is a post", "image.png");

    }
}
