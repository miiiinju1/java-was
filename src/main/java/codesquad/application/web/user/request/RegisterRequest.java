package codesquad.application.web.user.request;

public class RegisterRequest {
    private final String email;
    private final String userId;
    private final String password;
    private final String name;

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public RegisterRequest(String email, String userId, String password, String name) {
        this.email = email;
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

}
