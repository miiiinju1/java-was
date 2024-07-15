package codesquad.application.database.dao;

import codesquad.application.database.Database;
import codesquad.application.repository.vo.UserVO;

import java.util.Optional;

public interface UserDao extends Database<UserVO> {
    Optional<UserVO> findByUsername(String username);
}
