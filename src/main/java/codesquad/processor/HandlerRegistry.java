package codesquad.processor;

import codesquad.handler.HttpHandler;
import codesquad.http.HttpMethod;
import codesquad.http.Path;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public void registerHandler(HttpMethod httpMethod, String url, HttpHandler handler) {
        // PathVariable의 경우 {변수명}으로 표현되어 있으므로 해당 부분을 정규표현식으로 변경
        String regexPattern = url.replaceAll("\\{[^/]+\\}", "([^/]+)");

        handlerMappings.add(new HandlerMapping(httpMethod, Pattern.compile(regexPattern), handler));
    }


    public HandlerMapping getHandler(HttpMethod httpMethod, Path path) {
        for (HandlerMapping mapping : handlerMappings) {
            Matcher matcher = mapping.getPattern().matcher(path.getBasePath());
            if (matcher.matches() && mapping.getHttpMethod() == httpMethod) {
                return mapping;
            }
        }
        return null;
    }
}
