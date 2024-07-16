package codesquad.application.database.dao;

import codesquad.application.config.H2TestDatabaseConfig;
import codesquad.application.database.PostListVO;
import codesquad.application.database.PostVO;
import codesquad.application.database.UserVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

class PostDaoImplTest {

    private final H2TestDatabaseConfig h2TestDatabaseConfig = new H2TestDatabaseConfig();
    private final PostDaoImpl postDao = new PostDaoImpl(h2TestDatabaseConfig);

    @AfterEach
    void tearDown() {
        h2TestDatabaseConfig.resetDatabase();
    }

    @DisplayName("findAllJoinFetch: 모든 PostListVO 조회")
    @Test
    void findAllJoinFetch() {
        // given
        UserDao userDao = new UserDaoImpl(h2TestDatabaseConfig);
        UserVO userVO = new UserVO(null, "userId1", "password1", "name1", "email1", null);
        PostVO postVO = new PostVO(null, 1L, "content1", "/path/to/image1.jpg");

        userDao.save(userVO);
        postDao.save(postVO);

        // when
        List<PostListVO> allPosts = postDao.findAllJoinFetch();

        // then
        assertThat(allPosts).hasSize(1)
                .extracting("userId", "nickname", "content", "imagePath")
                .containsExactly(tuple(1L, "name1", "content1", "/path/to/image1.jpg"));

    }

    @DisplayName("save: 정상적인 PostVO 저장")
    @Test
    void savePostVO() {
        // given
        PostVO postVO = new PostVO(null, 1L, "content", "/path/to/image.jpg");

        // when
        long postId = postDao.save(postVO);

        // then
        Optional<PostVO> savedPostVO = postDao.findById(postId);
        assertThat(savedPostVO).isPresent()
                .get()
                .extracting("userId", "content", "imagePath")
                .containsExactly(1L, "content", "/path/to/image.jpg");
    }

    @DisplayName("findById: 존재하는 PostVO 조회")
    @Test
    void findByIdWithExistentPostVO() {
        // given
        PostVO postVO = new PostVO(null, 1L, "content", "/path/to/image.jpg");
        long 포스트_ID = postDao.save(postVO);

        // when
        Optional<PostVO> foundPostVO = postDao.findById(포스트_ID);

        // then
        assertThat(foundPostVO).isPresent()
                .get()
                .extracting("userId", "content", "imagePath")
                .containsExactly(1L, "content", "/path/to/image.jpg");
    }

    @DisplayName("findById: 존재하지 않는 PostVO 조회")
    @Test
    void findByIdWithNonExistentPostVO() {
        // given
        Long 없는_포스트_ID = 999L;

        // when
        Optional<PostVO> foundPostVO = postDao.findById(없는_포스트_ID);

        // then
        assertThat(foundPostVO).isEmpty();
    }

    @DisplayName("delete: 정상적인 PostVO 삭제")
    @Test
    void deletePostVO() {
        // given
        PostVO postVO = new PostVO(null, 1L, "content", "/path/to/image.jpg");
        long postId = postDao.save(postVO);

        // when
        postDao.delete(postId);

        // then
        Optional<PostVO> deletedPostVO = postDao.findById(postId);
        assertThat(deletedPostVO).isEmpty();
    }

    @DisplayName("update: 정상적인 PostVO 수정")
    @Test
    void updatePostVO() {
        // given
        PostVO postVO = new PostVO(null, 1L, "content", "/path/to/image.jpg");
        long 포스트_ID = postDao.save(postVO);

        PostVO updatedPostVO = new PostVO(포스트_ID, 1L, "updated content", "/new/path/to/image.jpg");

        // when
        postDao.update(포스트_ID, updatedPostVO);

        // then
        Optional<PostVO> foundPostVO = postDao.findById(포스트_ID);
        assertThat(foundPostVO).isPresent()
                .get()
                .extracting("userId", "content", "imagePath")
                .containsExactly(1L, "updated content", "/new/path/to/image.jpg");
    }

    @DisplayName("findAll: 저장된 모든 PostVO 조회")
    @Test
    void findAllPosts() {
        // given
        PostVO postVO1 = new PostVO(null, 1L, "content1", "/path/to/image1.jpg");
        PostVO postVO2 = new PostVO(null, 2L, "content2", "/path/to/image2.jpg");
        postDao.save(postVO1);
        postDao.save(postVO2);

        // when
        Collection<PostVO> allPosts = postDao.findAll();

        // then
        assertThat(allPosts).hasSize(2)
                .extracting("userId", "content", "imagePath")
                .containsExactlyInAnyOrder(
                        tuple(1L, "content1", "/path/to/image1.jpg"),
                        tuple(2L, "content2", "/path/to/image2.jpg")
                );
    }

    @DisplayName("save: content가 null인 경우 예외 발생")
    @Test
    void savePostVOWithNullContent() {
        // given
        PostVO postVO = new PostVO(null, 1L, null, "/path/to/image.jpg");

        // when & then
        assertThatThrownBy(() -> postDao.save(postVO))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("save: imagePath가 null인 경우 예외 발생")
    @Test
    void savePostVOWithNullImagePath() {
        // given
        PostVO postVO = new PostVO(null, 1L, "content", null);

        // when & then
        assertThatThrownBy(() -> postDao.save(postVO))
                .isInstanceOf(RuntimeException.class);
    }
}