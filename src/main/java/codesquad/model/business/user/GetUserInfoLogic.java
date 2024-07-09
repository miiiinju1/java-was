package codesquad.model.business.user;

import codesquad.authorization.AuthorizationContext;
import codesquad.authorization.AuthorizationContextHolder;
import codesquad.database.Database;
import codesquad.http.Session;
import codesquad.model.User;
import codesquad.processor.Triggerable;
import codesquad.web.user.response.UserInfoResponse;

public class GetUserInfoLogic implements Triggerable<Void, UserInfoResponse> {

    private final Database<User> userDatabase;

    public UserInfoResponse getUserInfo() {
        AuthorizationContext context = AuthorizationContextHolder.getContext();
        if (context == null) {
            // TODO 아래와 같이 UnauthorizedException을 던지는 거 곧 만들기
//            throw new UnauthorizedException();
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Session session = context.getSession();

        Long userPk = session.getUserPk();
        User user = userDatabase.findById(userPk)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserInfoResponse(user.getName());
    }

    public GetUserInfoLogic(Database<User> userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public UserInfoResponse run(Void unused) {
        return getUserInfo();
    }
}
