package codesquad.application.domain.post.business;

import codesquad.application.database.dao.CommentDao;
import codesquad.application.database.dao.PostDao;
import codesquad.application.domain.comment.response.CommentListResponse;
import codesquad.application.domain.comment.response.CommentResponse;
import codesquad.application.domain.post.response.PostListResponse;
import codesquad.application.domain.post.response.PostResponse;
import codesquad.application.processor.Triggerable;
import codesquad.application.repository.vo.CommentVO;

import java.util.List;

public class GetPostListLogic implements Triggerable<Void, PostListResponse> {

    private final PostDao postDao;
    private final CommentDao commentDao;

    @Override
    public PostListResponse run(Void unused) {
        // TODO nickname을 다른 걸로 변경하기

        // TODO 현재 단순 구현 용으로 반복 내에서 comment를 찾는 로직이 들어가 있음, connection도 많이 소비하게 될 테니, 한 connection을 사용할 수 있도록 만들어야하고,
        // TODO 해당 부분은 bulk로 처리할 수 있도록 만들어야 함
        List<PostResponse> postResponseList = postDao.findAll().stream()
                .map(post -> {
                    List<CommentVO> commentsOfPost = commentDao.findByPostId(post.postId());
                    List<CommentResponse> commentResponse = commentsOfPost.stream()
                            .map(comment -> new CommentResponse(comment.commentId(), "commentNickname", comment.content()))
                            .toList();
                    return new PostResponse(post.postId(), "nickname", post.content(), post.imagePath(), CommentListResponse.of(post.postId(), commentResponse));
                })
                .toList();

        return PostListResponse.of(postResponseList);
    }

    public GetPostListLogic(
            PostDao postDao,
            CommentDao commentDao
    ) {
        this.postDao = postDao;
        this.commentDao = commentDao;
    }
}
