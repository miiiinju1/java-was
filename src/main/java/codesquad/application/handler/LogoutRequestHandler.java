package codesquad.application.handler;

import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.webserver.authorization.AuthorizationContext;
import codesquad.webserver.authorization.AuthorizationContextHolder;
import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.http.Session;
import codesquad.webserver.middleware.SessionDatabase;

public class LogoutRequestHandler extends ApiRequestHandler<Void, Void> {
    @Override
    public void afterHandle(Void request, Void response, Request httpRequest, Response httpResponse) {
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
    public Void resolveArgument(Request httpRequest) {
        return null;
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, Response response) {
        // 이미 세션이 없는데 로그아웃을 시도할 경우
        response.setStatus(HttpStatus.BAD_REQUEST);
    }
}
