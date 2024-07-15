package codesquad.application.model.business;

import codesquad.application.database.dao.UserDao;
import codesquad.application.mapper.UserMapper;
import codesquad.application.model.User;
import codesquad.application.processor.Triggerable;
import codesquad.application.repository.vo.UserVO;
import codesquad.application.web.user.request.LoginRequest;

public class LoginUserLogic implements Triggerable<LoginRequest, User> {

    private final UserDao userDao;

    public LoginUserLogic(UserDao userDao) {
        this.userDao = userDao;
    }

    public User login(LoginRequest loginRequest) {
        final String userId = loginRequest.getUsername();
        final String password = loginRequest.getPassword();

        UserVO userVO = userDao.findByUsername(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 사용자가 없습니다."));

        User user = UserMapper.toUser(userVO);


        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Override
    public User run(LoginRequest request) {
        return login(request);
    }
}
