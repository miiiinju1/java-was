package codesquad.handler;

import codesquad.database.SessionDatabase;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.header.HttpHeaders;

public class LogoutRequestHandlerAdapter extends ApiRequestHandlerAdapter<Void, Void> {
    @Override
    public void afterHandle(Void request, Void response, HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpHeaders headers = httpRequest.getHeaders();

        String cookieValue = headers.getHeader("Cookie");
        SessionDatabase.delete(cookieValue);
        httpResponse.setStatus(HttpStatus.OK);

    }

    @Override
    public Void resolveArgument(HttpRequest httpRequest) {
        return null;
    }

    @Override
    public void applyExceptionHandler(RuntimeException e, HttpResponse response) {
        // 이미 세션이 없는데 로그아웃을 시도할 경우
        response.setStatus(HttpStatus.BAD_REQUEST);
    }
}
