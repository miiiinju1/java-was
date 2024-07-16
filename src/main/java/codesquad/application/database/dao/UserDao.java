package codesquad.application.database.dao;

import codesquad.application.database.Database;
import codesquad.application.database.UserVO;

import java.util.Optional;

public interface UserDao extends Database<UserVO> {
    Optional<UserVO> findByUsername(String username);
}
