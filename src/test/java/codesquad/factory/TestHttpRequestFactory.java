package codesquad.factory;

import codesquad.http.HttpRequest;

import java.util.Map;

public class TestHttpRequestFactory {

    public static HttpRequest createHttpRequest(
            String method,
            String path,
            Map<String, String> headers,
            String body
    ) {
        return HttpRequest.builder()
                .headers(headers)
                .path(path)
                .method(method)
                .version("HTTP/1.1")
                .body(body)
                .build();
    }
}
