package codesquad.application.handler;

import codesquad.application.model.User;
import codesquad.application.processor.argumentresolver.ArgumentResolver;
import codesquad.application.web.user.request.LoginRequest;
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
        response.setStatus(HttpStatus.FOUND);
        response.setHeader(HeaderConstants.LOCATION, "/users/login_failed");
    }

    @Override
    public void afterHandle(LoginRequest request, User response, Request httpRequest, Response httpResponse) {
        Session session = SessionDatabase.save(response.getUserPk());

        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setHeader(HeaderConstants.SET_COOKIE, "sid=" + session.getSessionId() + "; Path=/ ; Max-Age=" + session.getTimeout() + "; HttpOnly");
        httpResponse.setHeader(HeaderConstants.LOCATION, "/");
    }

    public LoginRequestHandler(ArgumentResolver<LoginRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
