package codesquad.application.domain.post.business;

import codesquad.application.database.dao.PostDao;
import codesquad.application.domain.post.response.PostListResponse;
import codesquad.application.domain.post.response.PostResponse;
import codesquad.application.processor.Triggerable;

import java.util.List;

public class GetPostListLogic implements Triggerable<Void, PostListResponse> {

    private final PostDao postDao;

    @Override
    public PostListResponse run(Void unused) {
        // TODO nickname을 다른 걸로 변경하기
        List<PostResponse> postResponseList = postDao.findAll().stream()
                .map(post -> new PostResponse(post.postId(), "nickname", post.content(), post.imagePath()))
                .toList();

        return PostListResponse.of(postResponseList);
    }

    public GetPostListLogic(PostDao postDao) {
        this.postDao = postDao;
    }
}
