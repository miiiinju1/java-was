package codesquad.application.domain.user.business;

import codesquad.application.database.dao.UserDao;
import codesquad.application.mapper.UserMapper;
import codesquad.application.domain.user.model.User;
import codesquad.application.processor.Triggerable;
import codesquad.application.domain.user.request.RegisterRequest;

public class RegisterUserLogic implements Triggerable<RegisterRequest, Long> {

    private final UserDao userDao;

    public Long registerUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String userId = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String name = registerRequest.getNickname();

        User user = new User(userId, password, name, email);
        long savedPk = userDao.save(UserMapper.toUserVO(user));
        if(savedPk == -1) {
            throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        }
        user.initUserPk(savedPk);

        return savedPk;
    }

    @Override
    public Long run(RegisterRequest request) {
        return registerUser(request);
    }
    public RegisterUserLogic(UserDao userDao) {
        this.userDao = userDao;
    }
}
