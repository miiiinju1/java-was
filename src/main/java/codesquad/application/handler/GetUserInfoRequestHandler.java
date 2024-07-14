package codesquad.application.handler;

import codesquad.application.web.user.response.UserInfoResponse;
import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.http.header.HeaderConstants;

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
    public void afterHandle(Void request, UserInfoResponse response, Request httpRequest, Response httpResponse) {
        httpResponse.getHttpHeaders().addHeader(HeaderConstants.CONTENT_TYPE, "application/json");
        httpResponse.setStatus(HttpStatus.OK);
    }

    @Override
    public Void resolveArgument(Request httpRequest) {
        return null;
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, Response response) {

    }
}
