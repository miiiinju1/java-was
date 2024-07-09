package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.header.HeaderConstants;
import codesquad.model.User;
import codesquad.processor.argumentresolver.ArgumentResolver;
import codesquad.web.user.LoginRequest;

public class LoginRequestHandlerAdapter extends ApiRequestHandlerAdapter<LoginRequest, User> {

    private final ArgumentResolver<LoginRequest> argumentResolver;

    public LoginRequestHandlerAdapter(ArgumentResolver<LoginRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }

    @Override
    public LoginRequest resolveArgument(HttpRequest httpRequest) {
        return argumentResolver.resolve(httpRequest);
    }

    @Override
    public void afterHandle(LoginRequest request, User response, HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.setHeader(HeaderConstants.SET_COOKIE, "logined=true; Path=/");
    }

}
