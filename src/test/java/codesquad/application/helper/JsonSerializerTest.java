package codesquad.application.helper;

import codesquad.application.domain.comment.response.CommentListResponse;
import codesquad.application.domain.comment.response.CommentResponse;
import codesquad.application.domain.post.response.PostListResponse;
import codesquad.application.domain.post.response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonSerializerTest {

    @DisplayName("PostListResponse를 JSON으로 직렬화")
    @Test
    void testToJson_PostListResponse() {
        // given
        CommentResponse commentResponse = new CommentResponse(1L, "user1", "Nice post!");
        CommentListResponse commentListResponse = new CommentListResponse(1L, List.of(commentResponse), 1L);
        PostResponse postResponse = new PostResponse(1L, "user1", "This is a post", "image.png", commentListResponse);
        PostListResponse postListResponse = new PostListResponse(List.of(postResponse), 1);

        // when
        String json = JsonSerializer.toJson(postListResponse);

        // then
        assertThat(json).isNotNull()
                .contains("\"postId\":1")
                .contains("\"nickname\":\"user1\"")
                .contains("\"content\":\"This is a post\"")
                .contains("\"imageName\":\"image.png\"")
                .contains("\"commentId\":1")
                .contains("\"commentCount\":1")
                .contains("\"totalCount\":1");
    }

    @DisplayName("PostResponse의 필드가 null일 때 JSON으로 직렬화")
    @Test
    void testToJson_PostResponseWithNullFields() {
        // given
        CommentResponse commentResponse = new CommentResponse(1L, null, "Nice post!");
        CommentListResponse commentListResponse = new CommentListResponse(1L, List.of(commentResponse), 1L);
        PostResponse postResponse = new PostResponse(1L, null, "This is a post", null, commentListResponse);
        PostListResponse postListResponse = new PostListResponse(List.of(postResponse), 1);

        // when
        String json = JsonSerializer.toJson(postListResponse);

        // then
        assertThat(json).isNotNull()
                .contains("\"postId\":1")
                .contains("\"nickname\"")
                .contains("\"content\":\"This is a post\"")
                .contains("\"imageName\"")
                .contains("\"commentId\":1")
                .contains("\"nickname\"")
                .contains("\"content\":\"Nice post!\"")
                .contains("\"commentCount\":1")
                .contains("\"totalCount\":1");
    }
}
