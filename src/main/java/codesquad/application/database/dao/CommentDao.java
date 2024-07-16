package codesquad.application.database.dao;

import codesquad.application.database.Database;
import codesquad.application.repository.vo.CommentVO;

import java.util.List;

public interface CommentDao extends Database<CommentVO> {
    List<CommentVO> findByPostId(Long postId);
}
