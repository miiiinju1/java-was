package codesquad.application.processor;

import codesquad.application.handler.HttpHandler;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.Path;

import java.util.HashSet;
import java.util.List;

public class HandlerRegistry {

    private final List<HandlerMapping<?, ?>> handlerMappings;
    public HandlerRegistry(List<HandlerMapping<?,?>> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }
    private final HashSet<String> registeredUrls = new HashSet<>();

    /**
     * 지정된 HTTP 메서드와 URL 패턴에 대해 핸들러를 등록합니다.
     * URL 패턴은 {변수명} 형태의 PathVariable을 포함할 수 있으며, 이는 매칭을 위해 정규 표현식으로 변환됩니다.
     *
     * @param httpMethod HTTP 메서드
     * @param url        URL 패턴 (PathVariable은 {변수명}으로 표현)
     * @param handler    등록할 핸들러
     */
    public <T, R> void registerHandler(HttpMethod httpMethod, String url, HttpHandler<T, R> handler, Triggerable<T, R> triggerable) {
        if(registeredUrls.contains(url+httpMethod)) {
            throw new IllegalArgumentException("이미 등록된 URL입니다.");
        }
        registeredUrls.add(url+httpMethod);
        handlerMappings.add(new HandlerMapping<>(httpMethod, url, handler, triggerable));
    }

    public HandlerMapping<?, ?> getHandler(HttpMethod httpMethod, Path path) {
        return handlerMappings.stream()
                .filter(mapping -> mapping.matchRequest(httpMethod, path.getBasePath()))
                .findFirst()
                .orElse(null);
    }
}
