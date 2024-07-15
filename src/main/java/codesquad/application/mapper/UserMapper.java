package codesquad.application.mapper;

import codesquad.application.model.User;
import codesquad.application.repository.vo.UserVO;

public class UserMapper {

    public static UserVO toUserVO(User user) {
        return new UserVO(
                user.getUserPk(),
                user.getUserId(),
                user.getPassword(),
                user.getEmail(),
                user.getName(),
                null
        );
    }

    public static User toUser(UserVO userVO) {
        User user = new User(userVO.username(), userVO.password(), userVO.name(), userVO.email());
        user.initUserPk(userVO.userId());
        return user;
    }

    private UserMapper() {
    }
}
