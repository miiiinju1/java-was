package codesquad.application.domain.post.business;

import codesquad.application.database.dao.PostDao;
import codesquad.application.database.dao.UserDao;
import codesquad.application.domain.post.request.PostCreateRequest;
import codesquad.application.mapper.PostMapper;
import codesquad.application.domain.post.model.Post;
import codesquad.application.processor.Triggerable;
import codesquad.webserver.authorization.AuthorizationContext;
import codesquad.webserver.authorization.AuthorizationContextHolder;
import codesquad.webserver.helper.FileSaveHelper;
import codesquad.webserver.http.Session;

public class PostCreateLogic implements Triggerable<PostCreateRequest, Void> {

    private final UserDao userDao;
    private final PostDao postDao;

    private void createPost(PostCreateRequest postCreateRequest) {

        AuthorizationContext authorizationContext = AuthorizationContextHolder.getContext();
        if(authorizationContext == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        Session session = authorizationContext.getSession();

        if (postCreateRequest.getContent().contains(",")) {
            throw new RuntimeException("콤마는 입력할 수 없습니다.");
        }
        Long userId = session.getUserId();

        // 파일 저장 후 path 분리
        try {
            String filename = FileSaveHelper.saveFile(postCreateRequest.getImage(), postCreateRequest.getImageName());

            // TODO 사용자가 존재하는지 확인하는 부분을 PostUpdater로 나중에 추상화해서 그 안에서 확인하기
            Post post = new Post(userId, postCreateRequest.getContent(), filename, null);

            postDao.save(PostMapper.toPostVO(post));
        } catch (Exception e) {
            throw new RuntimeException("파일 저장에 실패했습니다." + e.getMessage());
        }
    }

    @Override
    public Void run(PostCreateRequest request) {
        createPost(request);
        return null;
    }

    public PostCreateLogic(UserDao userDao, PostDao postDao) {
        this.userDao = userDao;
        this.postDao = postDao;
    }
}
