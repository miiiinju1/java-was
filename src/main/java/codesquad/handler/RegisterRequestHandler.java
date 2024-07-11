package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.header.HeaderConstants;
import codesquad.processor.argumentresolver.ArgumentResolver;
import codesquad.web.user.request.RegisterRequest;

import java.io.IOException;

public class RegisterRequestHandler extends ApiRequestHandler<RegisterRequest, Long> {

    private final ArgumentResolver<RegisterRequest> argumentResolver;

    @Override
    public RegisterRequest resolveArgument(HttpRequest httpRequest) {
        return argumentResolver.resolve(httpRequest);
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, HttpResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST);
        try {
            response.getBody().write(e.getMessage().getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void afterHandle(RegisterRequest request, Long response, HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.setStatus(HttpStatus.OK);
    }

    public RegisterRequestHandler(ArgumentResolver<RegisterRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
