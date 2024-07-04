package codesquad.processor;

import codesquad.handler.HttpHandlerAdapter;
import codesquad.http.HttpMethod;

import java.util.regex.Pattern;

public class HandlerMapping<R> {

    private final HttpMethod httpMethod;
    private final Pattern pattern;
    private final HttpHandlerAdapter<R> handler;
    private final Triggerable<R> triggerable;

    public HandlerMapping(HttpMethod httpMethod, Pattern pattern, HttpHandlerAdapter<R> handler, Triggerable<R> triggerable) {
        this.httpMethod = httpMethod;
        this.pattern = pattern;
        this.handler = handler;
        this.triggerable = triggerable;
    }

    public Triggerable<R> getTrigger() {
        return triggerable;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public HttpHandlerAdapter<R> getHandler() {
        return handler;
    }


}
