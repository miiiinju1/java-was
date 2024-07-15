package codesquad.application.web.user.response;

import codesquad.application.model.User;

import java.util.List;

public class UserListResponse {

    private final int count;
    private final List<UserInfo> userList;

    public int getCount() {
        return count;
    }

    public List<UserInfo> getUserList() {
        return userList;
    }

    public static UserListResponse of(List<User> users) {
        return new UserListResponse(users);
    }

    private UserListResponse(List<User> users) {
        this.userList = users.stream()
                    .map(u -> new UserInfo(u.getNickname(), u.getEmail()))
                    .toList();
        this.count = userList.size();
    }

    public static class UserInfo{

        private final String nickname;
        private final String email;

        public String getNickname() {
            return nickname;
        }

        public String getEmail() {
            return email;
        }

        public UserInfo(String nickname, String email) {
            this.nickname = nickname;
            this.email = email;
        }
    }
}
