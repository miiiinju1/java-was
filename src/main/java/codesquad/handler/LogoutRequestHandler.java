package codesquad.handler;

import codesquad.authorization.AuthorizationContext;
import codesquad.authorization.AuthorizationContextHolder;
import codesquad.database.SessionDatabase;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.Session;

public class LogoutRequestHandler extends ApiRequestHandler<Void, Void> {
    @Override
    public void afterHandle(Void request, Void response, HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!AuthorizationContextHolder.isAuthorized()) {
            httpResponse.setStatus(HttpStatus.BAD_REQUEST);
            return;
        }
        AuthorizationContext context = AuthorizationContextHolder.getContext();
        Session session = context.getSession();
        SessionDatabase.delete(session.getSessionId());
        httpResponse.setStatus(HttpStatus.OK);
    }

    @Override
    public Void resolveArgument(HttpRequest httpRequest) {
        return null;
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, HttpResponse response) {
        // 이미 세션이 없는데 로그아웃을 시도할 경우
        response.setStatus(HttpStatus.BAD_REQUEST);
    }
}
