package codesquad.processor;

import codesquad.handler.HttpHandler;
import codesquad.http.HttpMethod;

import java.util.regex.Pattern;

public class HandlerMapping {

    private final HttpMethod httpMethod;
    private final Pattern pattern;
    private final HttpHandler handler;

    public HandlerMapping(HttpMethod httpMethod, Pattern pattern, HttpHandler handler) {
        this.httpMethod = httpMethod;
        this.pattern = pattern;
        this.handler = handler;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public HttpHandler getHandler() {
        return handler;
    }


}
