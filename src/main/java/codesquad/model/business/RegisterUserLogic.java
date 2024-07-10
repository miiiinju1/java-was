package codesquad.model.business;

import codesquad.database.Database;
import codesquad.model.User;
import codesquad.processor.Triggerable;
import codesquad.web.user.request.RegisterRequest;

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
