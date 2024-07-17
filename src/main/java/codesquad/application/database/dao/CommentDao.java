package codesquad.application.database.dao;

import codesquad.application.database.Database;
import codesquad.application.database.vo.CommentListVO;
import codesquad.application.database.CommentVO;

import java.util.List;

public interface CommentDao extends Database<CommentVO> {
    List<CommentVO> findByPostId(Long postId);
    List<CommentListVO> findCommentsJoinFetch(Long postId);
}
