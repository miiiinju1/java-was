package codesquad.application.domain.user.handler;

import codesquad.application.handler.ApiRequestHandler;
import codesquad.application.domain.user.model.User;
import codesquad.application.processor.ArgumentResolver;
import codesquad.application.domain.user.request.LoginRequest;
import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.http.Session;
import codesquad.webserver.http.header.HeaderConstants;
import codesquad.webserver.middleware.SessionDatabase;

public class LoginRequestHandler extends ApiRequestHandler<LoginRequest, User> {

    private final ArgumentResolver<LoginRequest> argumentResolver;

    @Override
    public LoginRequest resolveArgument(Request httpRequest) {
        return argumentResolver.resolve(httpRequest);
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, Response response) {
        response.setStatus(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public void afterHandle(LoginRequest request, User response, Request httpRequest, Response httpResponse) {
        Session session = SessionDatabase.save(response.getUserId());

        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setHeader(HeaderConstants.SET_COOKIE, "sid=" + session.getSessionId() + "; Path=/ ; Max-Age=" + session.getTimeout() + "; HttpOnly");
        httpResponse.setHeader(HeaderConstants.LOCATION, "/");
    }

    public LoginRequestHandler(ArgumentResolver<LoginRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
