package codesquad.application.mapper;

import codesquad.application.model.User;
import codesquad.application.repository.vo.UserVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @DisplayName("toUserVO: User를 UserVO로 변환")
    @Test
    void toUserVO() {
        // given
        User user = new User("user123", "password", "name", "email@example.com");
        user.initUserPk(1L);

        // when
        UserVO userVO = UserMapper.toUserVO(user);

        // then
        assertThat(userVO)
                .extracting("userId", "username", "password", "name", "email")
                .containsExactly(1L, "user123", "password", "name", "email@example.com");
    }

    @DisplayName("toUser: UserVO를 User로 변환")
    @Test
    void toUser() {
        // given
        UserVO userVO = new UserVO(1L, "user123", "password", "name", "email@example.com", null);

        // when
        User user = UserMapper.toUser(userVO);

        // then
        assertThat(user)
                .extracting("userPk", "userId", "password", "name", "email")
                .containsExactly(1L, "user123", "password", "name", "email@example.com");
    }
}
