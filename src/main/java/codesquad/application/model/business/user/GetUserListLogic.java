package codesquad.application.model.business.user;

import codesquad.application.database.dao.UserDao;
import codesquad.application.mapper.UserMapper;
import codesquad.application.model.User;
import codesquad.application.processor.Triggerable;
import codesquad.application.web.user.response.UserListResponse;

import java.util.List;

public class GetUserListLogic implements Triggerable<Void, UserListResponse> {

    private final UserDao userDao;

    public UserListResponse getUserList() {
        // TODO Pagination 필요
        List<User> userList = userDao.findAll().stream()
                .map(UserMapper::toUser)
                .toList();

        return UserListResponse.of(userList);
    }

    public GetUserListLogic(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserListResponse run(Void unused) {
        return getUserList();
    }
}
