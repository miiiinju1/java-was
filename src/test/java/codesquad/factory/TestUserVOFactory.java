package codesquad.factory;

import codesquad.application.repository.vo.UserVO;

public class TestUserVOFactory {

    public static UserVO createDefaultUserVO() {
        return new UserVO(
                null,
                "username",
                "password",
                "name",
                "email",
                null
        );
    }
    public static UserVO createBy(
            String username,
            String name,
            String email
    ) {
        return new UserVO(
                null,
                username,
                "password",
                name,
                email,
                null
        );
    }

}
