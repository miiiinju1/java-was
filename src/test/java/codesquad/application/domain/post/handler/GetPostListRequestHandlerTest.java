package codesquad.application.domain.post.handler;

import codesquad.application.domain.comment.response.CommentListResponse;
import codesquad.application.domain.comment.response.CommentResponse;
import codesquad.application.domain.post.response.PostListResponse;
import codesquad.application.domain.post.response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetPostListRequestHandlerTest {

    private final GetPostListRequestHandler handler = new GetPostListRequestHandler();


    @DisplayName("serializeResponse: PostListResponse를 JSON으로 직렬화한다.")
    @Test
    void serializeResponse() {
        // given
        CommentResponse commentResponse = new CommentResponse(1L, "user1", "comment content", LocalDateTime.now());
        CommentListResponse commentListResponse = CommentListResponse.of(1L, List.of(commentResponse));
        PostResponse postResponse = new PostResponse(1L, "user1", "post content", "image.png", commentListResponse, LocalDateTime.now());
        PostListResponse postListResponse = PostListResponse.of(List.of(postResponse));

        // when
        String json = handler.serializeResponse(postListResponse);

        // then
        assertThat(json).isNotNull()
                .contains("\"postResponses\"")
                .contains("\"totalCount\":1")
                .contains("\"postId\":1")
                .contains("\"nickname\":\"user1\"")
                .contains("\"content\":\"post content\"")
                .contains("\"imageName\":\"image.png\"")
                .contains("\"commentList\"")
                .contains("\"commentId\":1")
                .contains("\"createdAt\"");
    }
}
