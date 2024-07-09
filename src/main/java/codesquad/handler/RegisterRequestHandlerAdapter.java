package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.processor.argumentresolver.ArgumentResolver;
import codesquad.web.user.RegisterRequest;

public class RegisterRequestHandlerAdapter extends ApiRequestHandlerAdapter<RegisterRequest, Long> {

    private final ArgumentResolver<RegisterRequest> argumentResolver;

    @Override
    public void afterHandle(RegisterRequest request, Long response, HttpRequest httpRequest, HttpResponse httpResponse) {
        // do nothing
        // TODO 여기에서 sendRedirect를 호출하면 되지 않을까?
    }

    @Override
    public RegisterRequest resolveArgument(HttpRequest httpRequest) {
        return argumentResolver.resolve(httpRequest);
    }

    public RegisterRequestHandlerAdapter(ArgumentResolver<RegisterRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
