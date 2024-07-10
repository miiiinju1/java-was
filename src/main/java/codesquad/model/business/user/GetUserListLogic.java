package codesquad.model.business.user;

import codesquad.database.Database;
import codesquad.model.User;
import codesquad.processor.Triggerable;
import codesquad.web.user.response.UserListResponse;

import java.util.ArrayList;
import java.util.List;

public class GetUserListLogic implements Triggerable<Void, UserListResponse> {

    private final Database<User> userDatabase;

    public UserListResponse getUserList() {
        // TODO Pagination 필요
        List<User> userList = new ArrayList<>(userDatabase.findAll());

        return UserListResponse.of(userList);
    }

    public GetUserListLogic(Database<User> userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public UserListResponse run(Void unused) {
        return getUserList();
    }
}
