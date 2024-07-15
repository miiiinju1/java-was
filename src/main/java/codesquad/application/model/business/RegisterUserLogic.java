package codesquad.application.model.business;

import codesquad.application.database.dao.UserDao;
import codesquad.application.mapper.UserMapper;
import codesquad.application.model.User;
import codesquad.application.processor.Triggerable;
import codesquad.application.web.user.request.RegisterRequest;

public class RegisterUserLogic implements Triggerable<RegisterRequest, Long> {

    private final UserDao userDao;

    public Long registerUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String userId = registerRequest.getUserId();
        String password = registerRequest.getPassword();
        String name = registerRequest.getName();

        User user = new User(userId, password, name, email);
        long savedPk = userDao.save(UserMapper.toUserVO(user));
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
