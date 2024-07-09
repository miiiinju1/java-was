package codesquad.http;

import codesquad.http.header.HttpHeaders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {

    private final HttpMethod method;
    private final Path path;
    private final HttpVersion version;
    private final HttpHeaders httpHeaders;
    private final String body;
    private final Map<String, Object> attributes;

    public HttpMethod getMethod() {
        return method;
    }

    public Path getPath() {
        return path;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpHeaders getHeaders() {
        return httpHeaders;
    }

    public String getBody() {
        return body;
    }

    public Optional<Object> getAttributes(String key) {
        validateAttributeKey(key);

        return Optional.ofNullable(attributes.get(key));
    }

    public void setAttributes(String key, Object value) {
        validateAttributeKey(key);
        validateAttributeValue(value);
        attributes.put(key, value);
    }

    private void validateAttributeKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("키는 null이거나 빈 문자열일 수 없습니다.");
        }
    }

    private void validateAttributeValue(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("값은 null일 수 없습니다.");
        }
    }
    public static Builder builder() {
        return new Builder();
    }

    public HttpRequest(String method, String path, String version, Map<String, String> headers, String body) {
        this.method = HttpMethod.of(method);
        this.path = Path.of(path);
        this.version = HttpVersion.of(version);
        this.httpHeaders = HttpHeaders.of(headers);
        this.body = body;
        this.attributes = new HashMap<>();
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                "\n, path='" + path + '\'' +
                "\n, version='" + version + '\'' +
                "\n, headers=" + httpHeaders +
                "\n, body='" + body + '\'' +
                '}';
    }

    public static class Builder {
        private String method;
        private String path;
        private String version;
        private Map<String, String> headers;
        private String body;

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(method, path, version, headers, body);
        }
    }

}
