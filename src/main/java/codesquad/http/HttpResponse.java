package codesquad.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpResponse {

    private final HttpVersion httpVersion;
    private final HttpStatus httpStatus;
    private final Map<String, String> headers;
    private final ByteArrayOutputStream body;

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public ByteArrayOutputStream getBody() {
        return body;
    }

    public HttpResponse(HttpVersion httpVersion, HttpStatus httpStatus, Map<String, String> headers, ByteArrayOutputStream body) {
        this.httpVersion = httpVersion;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse notFoundOf(String path) {
        byte[] responseBytes = ("<html><body><h1>404 Not Found " + path + "</h1></body></html>").getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        try {
            body.write(responseBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return HttpResponse.builder()
                .httpVersion(HttpVersion.HTTP_1_1)
                .httpStatus(HttpStatus.NOT_FOUND)
                .headers(Map.of("Content-Type", "text/html; charset=UTF-8"))
                .body(body)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpVersion httpVersion;
        private HttpStatus httpStatus;
        private Map<String, String> headers;
        private ByteArrayOutputStream body;

        public Builder httpVersion(HttpVersion httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(ByteArrayOutputStream body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(httpVersion, httpStatus, headers, body);
        }
    }

}
