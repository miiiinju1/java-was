package codesquad.application.web.user.request;

public class RegisterRequest {
    private final String email;
    private final String username;
    private final String password;
    private final String nickname;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public RegisterRequest(String email, String username, String password, String nickname) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

}
