package codesquad.model.business;

import codesquad.database.Database;
import codesquad.model.User;
import codesquad.processor.Triggerable;
import codesquad.web.user.RegisterRequest;

public class RegisterUserLogic implements Triggerable<RegisterRequest, Long> {

    private final Database<User> userDatabase;
    // TODO 임시 비즈니스 로직
    public Long registerUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String userId = registerRequest.getUserId();
        String password = registerRequest.getPassword();
        String name = registerRequest.getName();

        User user = new User(userId, password, name, email);

        return userDatabase.save(user);
    }

    @Override
    public Long run(RegisterRequest request) {
        return registerUser(request);
    }
    public RegisterUserLogic(Database<User> userDatabase) {
        this.userDatabase = userDatabase;
    }
}
