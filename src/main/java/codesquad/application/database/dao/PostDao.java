package codesquad.application.database.dao;

import codesquad.application.database.Database;
import codesquad.application.repository.vo.PostListVO;
import codesquad.application.repository.vo.PostVO;

import java.util.List;

public interface PostDao extends Database<PostVO> {

    List<PostListVO> findAllJoinFetch();

}
