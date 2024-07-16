package codesquad.application.domain.post.handler;

import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.application.domain.post.request.PostCreateRequest;
import codesquad.application.handler.ApiRequestHandler;
import codesquad.application.processor.ArgumentResolver;

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

    }

    public PostCreateRequestHandler(ArgumentResolver<PostCreateRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
