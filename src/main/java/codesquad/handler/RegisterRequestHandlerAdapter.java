package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.header.HeaderConstants;
import codesquad.processor.argumentresolver.ArgumentResolver;
import codesquad.web.user.RegisterRequest;

public class RegisterRequestHandlerAdapter extends ApiRequestHandlerAdapter<RegisterRequest, Long> {

    private final ArgumentResolver<RegisterRequest> argumentResolver;

    @Override
    public RegisterRequest resolveArgument(HttpRequest httpRequest) {
        return argumentResolver.resolve(httpRequest);
    }

    @Override
    public void afterHandle(RegisterRequest request, Long response, HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setHeader(HeaderConstants.LOCATION, "/");
    }

    public RegisterRequestHandlerAdapter(ArgumentResolver<RegisterRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
