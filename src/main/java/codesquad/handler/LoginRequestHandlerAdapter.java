package codesquad.handler;

import codesquad.database.SessionDatabase;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.Session;
import codesquad.http.header.HeaderConstants;
import codesquad.model.User;
import codesquad.processor.argumentresolver.ArgumentResolver;
import codesquad.web.user.LoginRequest;

public class LoginRequestHandlerAdapter extends ApiRequestHandlerAdapter<LoginRequest, User> {

    private final ArgumentResolver<LoginRequest> argumentResolver;

    @Override
    public LoginRequest resolveArgument(HttpRequest httpRequest) {
        return argumentResolver.resolve(httpRequest);
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, HttpResponse response) {
        response.setStatus(HttpStatus.FOUND);
        response.setHeader(HeaderConstants.LOCATION, "/users/login_failed");
    }

    @Override
    public void afterHandle(LoginRequest request, User response, HttpRequest httpRequest, HttpResponse httpResponse) {
        Session session = SessionDatabase.save(response.getUserPk());

        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setHeader(HeaderConstants.SET_COOKIE, "sid=" + session.getSessionId() + "; Path=/ ; Max-Age=" + session.getTimeout());// + "; HttpOnly");
        httpResponse.setHeader(HeaderConstants.LOCATION, "/");
    }

    public LoginRequestHandlerAdapter(ArgumentResolver<LoginRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
