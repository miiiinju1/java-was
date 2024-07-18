package codesquad.application.domain.post.handler;

import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.application.domain.post.request.PostCreateRequest;
import codesquad.application.handler.ApiRequestHandler;
import codesquad.application.processor.ArgumentResolver;
import codesquad.webserver.http.HttpStatus;

import java.io.IOException;

public class PostCreateRequestHandler extends ApiRequestHandler<PostCreateRequest, Void> {

    private final ArgumentResolver<PostCreateRequest> argumentResolver;

    @Override
    public void afterHandle(PostCreateRequest request, Void response, Request httpRequest, Response httpResponse) {

    }

    @Override
    public PostCreateRequest resolveArgument(Request httpRequest) {
        return argumentResolver.resolve(httpRequest);
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, Response response) throws IOException {
        if(e instanceof IllegalArgumentException) {
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        if(e.getMessage().equals("로그인이 필요합니다.")) {
            response.setStatus(HttpStatus.UNAUTHORIZED);

        }
    }

    public PostCreateRequestHandler(ArgumentResolver<PostCreateRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
