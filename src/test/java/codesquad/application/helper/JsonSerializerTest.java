package codesquad.application.helper;

import codesquad.application.domain.comment.response.CommentListResponse;
import codesquad.application.domain.comment.response.CommentResponse;
import codesquad.application.domain.post.response.PostListResponse;
import codesquad.application.domain.post.response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonSerializerTest {

    @DisplayName("PostListResponse를 JSON으로 직렬화")
    @Test
    void testToJson_PostListResponse() {
        // given
        LocalDateTime now = LocalDateTime.of(2021, 6, 1, 0, 0, 0, 0);
        CommentResponse commentResponse = new CommentResponse(1L, "user1", "Nice post!", now);
        CommentListResponse commentListResponse = new CommentListResponse(1L, List.of(commentResponse), 1L);
        PostResponse postResponse = new PostResponse(1L, "user1", "This is a post", "image.png", commentListResponse, now.minusDays(1));
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
                .contains("\"totalCount\":1")
                .contains("\"createdAt\":\"2021-05-31T00:00:00\"");
    }

    @DisplayName("PostResponse의 필드가 null일 때 JSON으로 직렬화")
    @Test
    void testToJson_PostResponseWithNullFields() {
        // given
        LocalDateTime now = LocalDateTime.of(2021, 6, 1, 0, 0, 0, 0);
        CommentResponse commentResponse = new CommentResponse(1L, null, "Nice post!", now);
        CommentListResponse commentListResponse = new CommentListResponse(1L, List.of(commentResponse), 1L);
        PostResponse postResponse = new PostResponse(1L, null, "This is a post", null, commentListResponse, now.minusDays(1));
        PostListResponse postListResponse = new PostListResponse(List.of(postResponse), 1);

        // when
        String json = JsonSerializer.toJson(postListResponse);

        // then
        assertThat(json).isNotNull()
                .contains("\"postId\":1")
                .contains("\"nickname\":null")
                .contains("\"content\":\"This is a post\"")
                .contains("\"imageName\":null")
                .contains("\"commentId\":1")
                .contains("\"nickname\":null")
                .contains("\"content\":\"Nice post!\"")
                .contains("\"commentCount\":1")
                .contains("\"totalCount\":1")
                .contains("\"createdAt\":\"2021-05-31T00:00:00\"");
    }
}
