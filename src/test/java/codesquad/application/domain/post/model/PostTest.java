package codesquad.application.domain.post.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostTest {
    @DisplayName("Post 생성 및 필드 값 확인")
    @Test
    void createPostAndCheckFields() {
        // given
        Long userId = 1L;
        String content = "This is a test content";
        String imagePath = "/path/to/image.jpg";

        // when
        Post post = new Post(userId, content, imagePath);

        // then
        assertThat(post)
                .extracting("userId", "content.value", "imagePath.value")
                .containsExactly(userId, content, imagePath);
    }

    @DisplayName("Post ID 초기화 및 확인")
    @Test
    void initAndCheckPostId() {
        // given
        Post post = new Post(1L, "content", "/path/to/image.jpg");
        Long postId = 100L;

        // when
        post.initPostId(postId);

        // then
        assertThat(post.getPostId()).isEqualTo(postId);
    }

    @DisplayName("Post 생성 시 userId가 null일 경우 예외 발생")
    @Test
    void createPostWithNullUserId() {
        // given
        Long userId = null;
        String content = "content";
        String imagePath = "/path/to/image.jpg";

        // when & then
        assertThatThrownBy(() -> new Post(userId, content, imagePath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자 아이디가 없습니다.");
    }

    @DisplayName("Post 생성 시 content가 null일 경우 예외 발생")
    @Test
    void createPostWithNullContent() {
        // given
        Long userId = 1L;
        String content = null;
        String imagePath = "/path/to/image.jpg";

        // when & then
        assertThatThrownBy(() -> new Post(userId, content, imagePath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내용이 없습니다.");
    }

    @DisplayName("Post 생성 시 content가 빈 문자열일 경우 예외 발생")
    @Test
    void createPostWithEmptyContent() {
        // given
        Long userId = 1L;
        String content = "";
        String imagePath = "/path/to/image.jpg";

        // when & then
        assertThatThrownBy(() -> new Post(userId, content, imagePath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내용이 없습니다.");
    }

    @DisplayName("Post 생성 시 content가 500자를 초과할 경우 예외 발생")
    @Test
    void createPostWithTooLongContent() {
        // given
        Long userId = 1L;
        String content = "a".repeat(501); // 501자
        String imagePath = "/path/to/image.jpg";

        // when & then
        assertThatThrownBy(() -> new Post(userId, content, imagePath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내용은 500자 이하여야 합니다.");
    }

    @DisplayName("Post 생성 시 imagePath가 null일 경우 예외 발생")
    @Test
    void createPostWithNullImagePath() {
        // given
        Long userId = 1L;
        String content = "content";
        String imagePath = null;

        // when & then
        assertThatThrownBy(() -> new Post(userId, content, imagePath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 경로가 없습니다.");
    }

    @DisplayName("Post 생성 시 imagePath가 빈 문자열일 경우 예외 발생")
    @Test
    void createPostWithEmptyImagePath() {
        // given
        Long userId = 1L;
        String content = "content";
        String imagePath = "";

        // when & then
        assertThatThrownBy(() -> new Post(userId, content, imagePath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 경로가 없습니다.");
    }
}