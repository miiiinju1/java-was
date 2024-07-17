package codesquad.application.mapper;

import codesquad.application.domain.user.model.User;
import codesquad.application.database.vo.UserVO;

public class UserMapper {

    public static UserVO toUserVO(User user) {
        return new UserVO(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getEmail(),
                null
        );
    }

    public static User toUser(UserVO userVO) {
        User user = new User(userVO.username(), userVO.password(), userVO.nickname(), userVO.email());
        user.initUserPk(userVO.userId());
        return user;
    }

    private UserMapper() {
    }
}
