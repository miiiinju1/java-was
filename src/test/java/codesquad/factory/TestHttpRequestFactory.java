package codesquad.factory;

import codesquad.http.HttpRequest;

import java.util.Map;

public class TestHttpRequestFactory {

    public static HttpRequest createGetResourceRequest(String path) {
        return HttpRequest.builder()
                .headers(Map.of("Host", "localhost:8080"))
                .path(path)
                .method("GET")
                .version("HTTP/1.1")
                .build();
    }
}
