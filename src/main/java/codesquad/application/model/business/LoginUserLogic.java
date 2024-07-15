package codesquad.application.model.business;

import codesquad.application.database.InMemoryDatabaseImpl;
import codesquad.application.processor.Triggerable;
import codesquad.application.web.user.request.LoginRequest;
import codesquad.application.model.User;

public class LoginUserLogic implements Triggerable<LoginRequest, User> {

    private final InMemoryDatabaseImpl<User> userDatabase;

    public LoginUserLogic(InMemoryDatabaseImpl<User> userDatabase) {
        this.userDatabase = userDatabase;
    }

    public User login(LoginRequest loginRequest) {
        final String userId = loginRequest.getUserId();
        final String password = loginRequest.getPassword();

        User user = userDatabase.findByCondition(u -> u.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 사용자가 없습니다."));

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
