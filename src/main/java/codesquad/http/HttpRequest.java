package codesquad.http;

import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final String body;

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public static Builder builder() {
        return new Builder();
    }

    public HttpRequest(String method, String path, String version, Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                "\n, path='" + path + '\'' +
                "\n, version='" + version + '\'' +
                "\n, headers=" + headers +
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
