package codesquad.application.database.dao;

import codesquad.application.database.Database;
import codesquad.application.database.PostListVO;
import codesquad.application.database.PostVO;

import java.util.List;

public interface PostDao extends Database<PostVO> {

    List<PostListVO> findAllJoinFetch();

}
