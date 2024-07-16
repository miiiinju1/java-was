package codesquad.application.domain.post.handler;

import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.application.domain.post.response.PostListResponse;
import codesquad.application.handler.ApiRequestHandler;
import codesquad.application.helper.JsonSerializer;

import java.io.IOException;

public class GetPostListRequestHandler extends ApiRequestHandler<Void, PostListResponse> {

    public String serializeResponse(PostListResponse response) {
        return JsonSerializer.toJson(response);
    }

    @Override
    public void afterHandle(Void request, PostListResponse response, Request httpRequest, Response httpResponse) {

    }

    @Override
    public Void resolveArgument(Request httpRequest) {
        return null;
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, Response response) throws IOException {

    }
}
