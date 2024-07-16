package codesquad.application.domain.post.argumentresolver;

import codesquad.api.Request;
import codesquad.application.domain.post.request.PostCreateRequest;
import codesquad.application.processor.argumentresolver.ArgumentResolver;
import codesquad.webserver.helper.MultiPartParseHelper;
import codesquad.webserver.http.header.HttpHeaders;

import java.io.IOException;
import java.util.Map;

public class PostCreateArgumentResolver implements ArgumentResolver<PostCreateRequest> {

    @Override
    public PostCreateRequest resolve(final Request httpRequest) {

        final HttpHeaders headers = httpRequest.getHeaders();
        final String boundary = headers.getMultipartBoundary()
                .orElseThrow(() -> new RuntimeException("Content-Type이 multipart/form-data가 아닙니다."));

        try {
            Map<String, MultiPartParseHelper.MultiPart> parse = MultiPartParseHelper.parse(httpRequest.getBody(), boundary);

            String content = parse.get("content").getTextContent();

            String imageName = parse.get("image").getFilename();
            byte[] image = parse.get("image").getContent();
            return new PostCreateRequest(content, imageName, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
