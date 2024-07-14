package codesquad.application.model;

public class User {
    private Long userPk;
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Long getUserPk() {
        return userPk;
    }

    public void initUserPk(Long userPk) {
        if(this.userPk != null) {
            throw new IllegalArgumentException("이미 PK가 존재합니다.");
        }
        this.userPk = userPk;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }



}
