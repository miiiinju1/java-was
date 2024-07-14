package codesquad.application.handler;

import codesquad.application.web.user.response.UserListResponse;
import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.webserver.http.HttpStatus;

import java.util.stream.Collectors;

public class GetUserListRequestHandler extends ApiRequestHandler<Void, UserListResponse> {


    @Override
    public String serializeResponse(UserListResponse response) {

        // TODO 외부 직렬화기 주입 받도록 수정하기
        String userListJson = response.getUserList().stream()
                .map(userInfo -> """
                {
                    "name": "%s",
                    "email": "%s"
                }
                """.formatted(userInfo.getName(), userInfo.getEmail()))
                .collect(Collectors.joining(", ", "[", "]"));

        return """
            {
                "count": %d,
                "userList": %s
            }
            """.formatted(response.getCount(), userListJson);
    }


    @Override
    public void afterHandle(Void request, UserListResponse response, Request httpRequest, Response httpResponse) {
        httpResponse.getHttpHeaders().addHeader("Content-Type", "application/json");
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
