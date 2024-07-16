package codesquad.application.domain.user.response;

public class UserInfoResponse {

    private final String name;

    public String getName() {
        return name;
    }

    public UserInfoResponse(String name) {
        this.name = name;
    }
}
