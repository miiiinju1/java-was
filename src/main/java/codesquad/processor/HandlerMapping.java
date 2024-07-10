package codesquad.processor;

import codesquad.handler.HttpHandler;
import codesquad.http.HttpMethod;

import java.util.regex.Pattern;

public class HandlerMapping<T, R> {

    private final HttpMethod httpMethod;
    private final Pattern pattern;
    private final HttpHandler<T, R> handler;
    private final Triggerable<T, R> triggerable;
    private static final Pattern DISALLOWED_SPECIAL_CHARACTERS_PATTERN = Pattern.compile("[!@#$%^&*()+=|<>?\\[\\]~]");

    public HandlerMapping(HttpMethod httpMethod, String url, HttpHandler<T, R> handler, Triggerable<T, R> triggerable) {
        this.httpMethod = validateHttpMethod(httpMethod);
        this.pattern = transformUrlToRegexPattern(url);
        this.handler = validateHandler(handler);
        this.triggerable = validateTriggerable(triggerable);
    }

    public Triggerable<T, R> getTrigger() {
        return triggerable;
    }

    public HttpHandler<T, R> getHandler() {
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

    private HttpHandler<T, R> validateHandler(HttpHandler<T, R> handler) {
        if(handler == null) {
            throw new IllegalArgumentException("handler가 null입니다.");
        }
        return handler;
    }

    private Pattern transformUrlToRegexPattern(String url) {
        if(url == null || url.isEmpty()) {
            throw new IllegalArgumentException("url이 null이거나 비어있습니다.");
        }

        if (DISALLOWED_SPECIAL_CHARACTERS_PATTERN.matcher(url).find()) {
            throw new IllegalArgumentException("url에 허용되지 않은 특수 문자가 포함될 수 없습니다.");
        }

        // PathVariable의 경우 {변수명}으로 표현되어 있으므로 해당 부분을 정규표현식으로 변경
        String regexPattern = url.replaceAll("\\{[^/]+\\}", "([^/]+)");
        regexPattern = "^" + regexPattern + "$";
        return Pattern.compile(regexPattern);
    }

    private Triggerable<T, R> validateTriggerable(Triggerable<T, R> triggerable) {
        if(triggerable == null) {
            throw new IllegalArgumentException("triggerable이 null입니다.");
        }
        return triggerable;
    }

}
