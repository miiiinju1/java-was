package codesquad.application.domain.comment.argumentresolver;

import codesquad.api.Request;
import codesquad.application.domain.comment.request.CreateCommentRequest;
import codesquad.application.helper.JsonDeserializer;
import codesquad.application.processor.ArgumentResolver;
import codesquad.webserver.http.Path;

import java.util.Map;

public class CreateCommentArgumentResolver implements ArgumentResolver<CreateCommentRequest> {
    @Override
    public CreateCommentRequest resolve(Request httpRequest) {
        //주소에서
        Path path = httpRequest.getPath();
        Long postId = Long.parseLong(path.getSegments().get(2));

        String body = new String(httpRequest.getBody().readAllBytes());

        Map<String, String> stringStringMap = JsonDeserializer.simpleConvertJsonToMap(body);

        String content = stringStringMap.get("content");

        if (postId == null || content == null) {
            throw new RuntimeException("필수 파라미터가 누락되었습니다.");
        }
        return new CreateCommentRequest(postId, content);
    }
}
