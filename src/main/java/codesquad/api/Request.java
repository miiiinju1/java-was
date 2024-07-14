package codesquad.api;

import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpVersion;
import codesquad.webserver.http.Path;
import codesquad.webserver.http.header.HttpHeaders;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public interface Request {
    HttpMethod getMethod();

    Path getPath();

    HttpVersion getVersion();

    HttpHeaders getHeaders();

    ByteArrayInputStream getBody();

    Optional<Object> getAttributes(String key);

    void setAttributes(String key, Object value);
}
