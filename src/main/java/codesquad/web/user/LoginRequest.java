package codesquad.web.user;

public class LoginRequest {

    private final String email;
    private final String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
