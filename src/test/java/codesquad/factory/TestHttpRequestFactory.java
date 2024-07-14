package codesquad.factory;

import codesquad.webserver.http.HttpRequest;

import java.util.List;
import java.util.Map;

public class TestHttpRequestFactory {

    public static HttpRequest createGetResourceRequest(String path) {
        return HttpRequest.builder()
                .headers(Map.of("Host", List.of("localhost:8080")))
                .path(path)
                .method("GET")
                .version("HTTP/1.1")
                .build();
    }
}
