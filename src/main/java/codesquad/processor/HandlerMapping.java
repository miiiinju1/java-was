package codesquad.processor;

import codesquad.handler.HttpHandlerAdapter;
import codesquad.http.HttpMethod;

import java.util.regex.Pattern;

public class HandlerMapping<T, R> {

    private final HttpMethod httpMethod;
    private final Pattern pattern;
    private final HttpHandlerAdapter<T, R> handler;
    private final Triggerable<T, R> triggerable;

    public HandlerMapping(HttpMethod httpMethod, String url, HttpHandlerAdapter<T, R> handler, Triggerable<T, R> triggerable) {
        this.httpMethod = validateHttpMethod(httpMethod);
        this.pattern = transformUrlToRegexPattern(url);
        this.handler = validateHandler(handler);
        this.triggerable = validateTriggerable(triggerable);
    }

    public Triggerable<T, R> getTrigger() {
        return triggerable;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public HttpHandlerAdapter<T, R> getHandler() {
        return handler;
    }

    public boolean matchRequest(final HttpMethod httpMethod, final String basePath) {
        return this.httpMethod.equals(httpMethod) && pattern.matcher(basePath).matches();
    }

    private HttpMethod validateHttpMethod(HttpMethod httpMethod) {
        if(httpMethod == null) {
            throw new IllegalArgumentException("httpMethod이 null입니다.");
        }
        return httpMethod;
    }

    private HttpHandlerAdapter<T, R> validateHandler(HttpHandlerAdapter<T, R> handler) {
        if(handler == null) {
            throw new IllegalArgumentException("handler가 null입니다.");
        }
        return handler;
    }

    private Pattern transformUrlToRegexPattern(String url) {
        if(url == null || url.isEmpty()) {
            throw new IllegalArgumentException("url이 null이거나 비어있습니다.");
        }

        // PathVariable의 경우 {변수명}으로 표현되어 있으므로 해당 부분을 정규표현식으로 변경
        String regexPattern = url.replaceAll("\\{[^/]+\\}", "([^/]+)");
        return Pattern.compile(regexPattern);
    }

    private Triggerable<T, R> validateTriggerable(Triggerable<T, R> triggerable) {
        if(triggerable == null) {
            throw new IllegalArgumentException("triggerable이 null입니다.");
        }
        return triggerable;
    }

}
