package codesquad.application.model.business;

import codesquad.application.database.Database;
import codesquad.application.processor.Triggerable;
import codesquad.application.web.user.request.RegisterRequest;
import codesquad.application.model.User;

public class RegisterUserLogic implements Triggerable<RegisterRequest, Long> {

    private final Database<User> userDatabase;

    public Long registerUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String userId = registerRequest.getUserId();
        String password = registerRequest.getPassword();
        String name = registerRequest.getName();

        User user = new User(userId, password, name, email);
        long savedPk = userDatabase.save(user);
        user.initUserPk(savedPk);

        return savedPk;
    }

    @Override
    public Long run(RegisterRequest request) {
        return registerUser(request);
    }
    public RegisterUserLogic(Database<User> userDatabase) {
        this.userDatabase = userDatabase;
    }
}
