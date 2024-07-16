package codesquad.application.domain.user.business;

import codesquad.application.database.dao.UserDao;
import codesquad.application.processor.Triggerable;
import codesquad.application.database.UserVO;
import codesquad.application.domain.user.response.UserInfoResponse;
import codesquad.webserver.authorization.AuthorizationContext;
import codesquad.webserver.authorization.AuthorizationContextHolder;
import codesquad.webserver.http.Session;

public class GetUserInfoLogic implements Triggerable<Void, UserInfoResponse> {

    private final UserDao userDao;

    public UserInfoResponse getUserInfo() {
        AuthorizationContext context = AuthorizationContextHolder.getContext();
        if (context == null) {
            // TODO 아래와 같이 UnauthorizedException을 던지는 거 곧 만들기
//            throw new UnauthorizedException();
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Session session = context.getSession();

        Long userPk = session.getUserId();
        UserVO userVO = userDao.findById(userPk)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserInfoResponse(userVO.nickname());
    }

    public GetUserInfoLogic(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserInfoResponse run(Void unused) {
        return getUserInfo();
    }
}
