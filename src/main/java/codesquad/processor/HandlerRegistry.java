package codesquad.processor;

import codesquad.handler.HttpHandlerAdapter;
import codesquad.http.HttpMethod;
import codesquad.http.Path;

import java.util.List;
import java.util.regex.Matcher;

public class HandlerRegistry {

    private final List<HandlerMapping<?, ?>> handlerMappings;
    public HandlerRegistry(List<HandlerMapping<?,?>> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    /**
     * 지정된 HTTP 메서드와 URL 패턴에 대해 핸들러를 등록합니다.
     * URL 패턴은 {변수명} 형태의 PathVariable을 포함할 수 있으며, 이는 매칭을 위해 정규 표현식으로 변환됩니다.
     *
     * @param httpMethod HTTP 메서드
     * @param url        URL 패턴 (PathVariable은 {변수명}으로 표현)
     * @param handler    등록할 핸들러
     */
    public <T, R> void registerHandler(HttpMethod httpMethod, String url, HttpHandlerAdapter<T, R> handler, Triggerable<T, R> triggerable) {
        handlerMappings.add(new HandlerMapping<>(httpMethod, url, handler, triggerable));
    }

    public HandlerMapping<?, ?> getHandler(HttpMethod httpMethod, Path path) {
        for (HandlerMapping<?, ?> mapping : handlerMappings) {
            Matcher matcher = mapping.getPattern().matcher(path.getBasePath());
            if (matcher.matches() && mapping.getHttpMethod() == httpMethod) {
                return mapping;
            }
        }
        return null;
    }
}
