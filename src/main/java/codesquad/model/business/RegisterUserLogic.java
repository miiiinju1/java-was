package codesquad.model.business;

import codesquad.model.User;
import codesquad.processor.Triggerable;
import codesquad.web.user.RegisterRequest;

public class RegisterUserLogic implements Triggerable<RegisterRequest, Void> {

    // TODO 임시 비즈니스 로직
    public void registerUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String userId = registerRequest.getUserId();
        String password = registerRequest.getPassword();
        String name = registerRequest.getName();

        User user = new User(userId, password, name, email);
        System.out.println("user = " + user);
    }

    @Override
    public Void run(RegisterRequest request) {
        registerUser(request);
        return null;
    }
}
