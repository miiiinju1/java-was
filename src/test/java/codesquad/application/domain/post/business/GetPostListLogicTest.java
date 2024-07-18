package codesquad.application.domain.post.business;

import codesquad.application.config.CSVTestDatabaseConfig;
import codesquad.application.database.dao.*;
import codesquad.application.domain.post.response.PostListResponse;
import codesquad.application.helper.Base64Util;
import codesquad.csvdb.jdbc.CsvExecutor;
import codesquad.factory.TestCommentVOFacotory;
import codesquad.factory.TestPostVOFacotry;
import codesquad.factory.TestUserVOFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GetPostListLogicTest {

    private final CSVTestDatabaseConfig csvTestDatabaseConfig = new CSVTestDatabaseConfig();
    private final PostDao postDao = new PostDaoImpl(csvTestDatabaseConfig);
    private final CommentDao commentDao = new CommentDaoImpl(csvTestDatabaseConfig);
    private final UserDao userDao = new UserDaoImpl(csvTestDatabaseConfig);
    private final GetPostListLogic getPostListLogic = new GetPostListLogic(postDao, commentDao);

    @BeforeEach
    void setUp() {
        CsvExecutor.clear();
    }

    @AfterEach
    void tearDown() {
        csvTestDatabaseConfig.resetDatabase();
        CsvExecutor.clear();
    }

    @DisplayName("run: 전체 포스트와 Comment를 포함하는 PostListResponse를 반환한다.")
    @Test
    void getPostListLogic() {
        // given
        long 사용자_ID_1 = userDao.save(TestUserVOFactory.createBy("userId1", "nickname1", "email1"));
        long 사용자_ID_2 = userDao.save(TestUserVOFactory.createBy("userId2", "nickname2", "email2"));

        long 포스트_ID_1 = postDao.save(TestPostVOFacotry.createDefaultPostVO(사용자_ID_1));
        long 포스트_ID_2 = postDao.save(TestPostVOFacotry.createDefaultPostVO(사용자_ID_2));

        commentDao.save(TestCommentVOFacotory.createBy(포스트_ID_1, 사용자_ID_1, Base64Util.encode("comment1")));
        commentDao.save(TestCommentVOFacotory.createBy(포스트_ID_2, 사용자_ID_2, Base64Util.encode("comment2")));

        // when
        PostListResponse postListResponse = getPostListLogic.run(null);


        // then
        assertThat(postListResponse)
                .isNotNull()
                .satisfies(plr -> {
                    assertThat(plr.postResponses()).hasSize(2);
                    assertThat(plr.totalCount()).isEqualTo(2);

                    assertThat(plr.postResponses().get(0))
                            .satisfies(post -> {
                                assertThat(post)
                                        .extracting( "nickname", "content", "imageName")
                                        .contains("nickname2", "content", "imagePath");
                                assertThat(post.commentList()).isNotNull()
                                        .satisfies(clr -> {
                                            assertThat(clr.commentCount()).isEqualTo(1L);

                                            assertThat(clr.commentList()).hasSize(1);
                                            assertThat(clr.commentList().get(0))
                                                    .satisfies(comment -> {
                                                        assertThat(comment)
                                                                .extracting("nickname", "content")
                                                                .contains( "nickname2", "comment2");
                                                        assertThat(comment.createdAt()).isNotNull();
                                                    });
                                        });
                            });

                    assertThat(plr.postResponses().get(1))
                            .satisfies(post -> {
                                assertThat(post)
                                        .extracting("nickname", "content", "imageName")
                                        .contains("nickname1", "content", "imagePath");
                                assertThat(post.commentList()).isNotNull()
                                        .satisfies(clr -> {
                                            assertThat(clr.commentCount()).isEqualTo(1L);

                                            assertThat(clr.commentList().get(0))
                                                    .satisfies(comment -> {
                                                        assertThat(comment)
                                                                .extracting( "nickname", "content")
                                                                .contains( "nickname1", "comment1");
                                                        assertThat(comment.createdAt()).isNotNull();
                                                    });
                                        });
                            });
                });
    }
}