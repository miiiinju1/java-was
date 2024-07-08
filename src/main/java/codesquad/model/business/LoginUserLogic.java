package codesquad.model.business;

import codesquad.database.Database;
import codesquad.model.User;
import codesquad.processor.Triggerable;
import codesquad.web.user.LoginRequest;

public class LoginUserLogic implements Triggerable<LoginRequest, User> {

    private final Database<User> userDatabase;

    public LoginUserLogic(Database<User> userDatabase) {
        this.userDatabase = userDatabase;
    }

    public User login(LoginRequest loginRequest) {
        final String userId = loginRequest.getUserId();
        final String password = loginRequest.getPassword();

        User user = userDatabase.findByCondition(u -> u.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자가 없습니다."));

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
