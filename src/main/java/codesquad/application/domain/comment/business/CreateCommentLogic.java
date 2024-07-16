package codesquad.application.domain.comment.business;

import codesquad.application.database.dao.CommentDao;
import codesquad.application.domain.comment.request.CreateCommentRequest;
import codesquad.application.mapper.CommentMapper;
import codesquad.application.domain.comment.model.Comment;
import codesquad.application.processor.Triggerable;
import codesquad.webserver.authorization.AuthorizationContextHolder;
import codesquad.webserver.http.Session;

public class CreateCommentLogic implements Triggerable<CreateCommentRequest, Void> {

    private final CommentDao commentDao;

    private void createComment(CreateCommentRequest createCommentRequest) {

        Session session = AuthorizationContextHolder.getContext().getSession();

        Long userId = session.getUserId();
        Comment comment = new Comment(createCommentRequest.postId(), userId, createCommentRequest.content(), null);

        commentDao.save(CommentMapper.toCommentVO(comment));
    }

    @Override
    public Void run(CreateCommentRequest createCommentRequest) {
        createComment(createCommentRequest);
        return null;
    }

    public CreateCommentLogic(CommentDao commentDao) {
        this.commentDao = commentDao;
    }
}
