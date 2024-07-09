package codesquad.http;

import codesquad.http.header.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpResponse {

    private final HttpVersion httpVersion;
    private HttpStatus httpStatus;
    private HttpHeaders httpHeaders;
    private final ByteArrayOutputStream body;

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public ByteArrayOutputStream getBody() {
        return body;
    }

    public void setHeader(String key, String value) {
        httpHeaders.addHeader(key, value);
    }

    public void setStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpResponse(HttpVersion httpVersion) {
        this.httpVersion = validateHttpVersion(httpVersion);
        this.httpStatus = HttpStatus.INITIAL_STATUS;
        this.httpHeaders = HttpHeaders.emptyHeader();
        this.body = new ByteArrayOutputStream();
    }

    public static HttpResponse internalServerErrorOf(String path) {
        byte[] responseBytes = ("<html><body><h1>500 Internal Server Error " + path + "</h1></body></html>").getBytes(StandardCharsets.UTF_8);
        HttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_1_1);
        httpResponse.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        httpResponse.httpHeaders = HttpHeaders.of(Map.of("Content-Type", "text/html; charset=UTF-8"));
        httpResponse.body.write(responseBytes, 0, responseBytes.length);

        return httpResponse;
    }

    public static HttpResponse notFoundOf(String path) {
        byte[] responseBytes = ("<html><body><h1>404 Not Found " + path + "</h1></body></html>").getBytes(StandardCharsets.UTF_8);
        HttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_1_1);
        httpResponse.httpStatus = HttpStatus.NOT_FOUND;
        httpResponse.httpHeaders = HttpHeaders.of(Map.of("Content-Type", "text/html; charset=UTF-8"));
        httpResponse.body.write(responseBytes, 0, responseBytes.length);

        return httpResponse;
    }

    private HttpVersion validateHttpVersion(HttpVersion httpVersion) {
        if (httpVersion == null) {
            throw new IllegalArgumentException("HttpVersion이 존재하지 않습니다.");
        }
        return httpVersion;
    }

}
