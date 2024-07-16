package codesquad.application.handler;

import codesquad.application.processor.argumentresolver.ArgumentResolver;
import codesquad.application.web.user.request.RegisterRequest;
import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.webserver.http.HttpStatus;

import java.io.IOException;

public class RegisterRequestHandler extends ApiRequestHandler<RegisterRequest, Long> {

    private final ArgumentResolver<RegisterRequest> argumentResolver;

    @Override
    public RegisterRequest resolveArgument(Request httpRequest) {
        return argumentResolver.resolve(httpRequest);
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, Response response) throws IOException {
        if (e instanceof IllegalArgumentException) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.getBody().write(e.getMessage().getBytes());
        } else if (e instanceof RuntimeException) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.getBody().write(e.getMessage().getBytes());
        }
    }

    @Override
    public void afterHandle(RegisterRequest request, Long response, Request httpRequest, Response httpResponse) {
        httpResponse.setStatus(HttpStatus.OK);
    }

    public RegisterRequestHandler(ArgumentResolver<RegisterRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
