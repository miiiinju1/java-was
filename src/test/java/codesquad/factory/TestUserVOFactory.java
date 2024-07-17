package codesquad.factory;

import codesquad.application.database.vo.UserVO;

public class TestUserVOFactory {

    public static UserVO createDefaultUserVO() {
        return new UserVO(
                null,
                "username",
                "password",
                "nickname",
                "email",
                null
        );
    }
    public static UserVO createBy(
            String username,
            String nickname,
            String email
    ) {
        return new UserVO(
                null,
                username,
                "password",
                nickname,
                email,
                null
        );
    }

}
