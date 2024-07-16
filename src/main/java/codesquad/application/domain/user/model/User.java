package codesquad.application.domain.user.model;

public class User {
    private Long userId;
    private final String username;
    private final String password;
    private final String nickname;
    private final String email;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public Long getUserId() {
        return userId;
    }

    public void initUserPk(Long userPk) {
        if(this.userId != null) {
            throw new IllegalArgumentException("이미 PK가 존재합니다.");
        }
        this.userId = userPk;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public User(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }



}
