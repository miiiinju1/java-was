package codesquad.application.domain.comment.handler;

import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.application.domain.comment.request.CreateCommentRequest;
import codesquad.application.handler.ApiRequestHandler;
import codesquad.application.processor.argumentresolver.ArgumentResolver;

import java.io.IOException;

public class CreateCommentRequestHandler extends ApiRequestHandler<CreateCommentRequest, Void> {

    private final ArgumentResolver<CreateCommentRequest> argumentResolver;

    @Override
    public void afterHandle(CreateCommentRequest request, Void response, Request httpRequest, Response httpResponse) {

    }

    @Override
    public CreateCommentRequest resolveArgument(Request httpRequest) {
        return argumentResolver.resolve(httpRequest);
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, Response response) throws IOException {

    }

    public CreateCommentRequestHandler(ArgumentResolver<CreateCommentRequest> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
}
