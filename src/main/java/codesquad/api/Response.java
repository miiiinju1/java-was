package codesquad.api;

import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.http.HttpVersion;
import codesquad.webserver.http.header.HttpHeaders;

import java.io.ByteArrayOutputStream;

public interface Response {
    HttpVersion getHttpVersion();

    HttpStatus getHttpStatus();

    HttpHeaders getHttpHeaders();

    ByteArrayOutputStream getBody();

    void setHeader(String key, String value);

    void setStatus(HttpStatus httpStatus);
}
