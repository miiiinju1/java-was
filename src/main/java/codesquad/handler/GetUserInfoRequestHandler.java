package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.header.HeaderConstants;
import codesquad.web.user.response.UserInfoResponse;

public class GetUserInfoRequestHandler extends ApiRequestHandler<Void, UserInfoResponse> {

    @Override
    public String serializeResponse(UserInfoResponse response) {
        // 동작 확인만을 위한 코드, 실제로는 직렬화기를 사용할 것
        return """
                {
                    "name": "%s"
                }
                                
                """.formatted(response.getName());
    }

    @Override
    public void afterHandle(Void request, UserInfoResponse response, HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.getHttpHeaders().addHeader(HeaderConstants.CONTENT_TYPE, "application/json");
        httpResponse.setStatus(HttpStatus.OK);
    }

    @Override
    public Void resolveArgument(HttpRequest httpRequest) {
        return null;
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, HttpResponse response) {

    }
}
