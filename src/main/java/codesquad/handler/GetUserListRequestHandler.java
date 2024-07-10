package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.web.user.response.UserListResponse;

import java.util.stream.Collectors;

public class GetUserListRequestHandler extends ApiRequestHandlerAdapter<Void, UserListResponse> {


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
    public void afterHandle(Void request, UserListResponse response, HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.getHttpHeaders().addHeader("Content-Type", "application/json");
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
