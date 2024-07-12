package codesquad.processor;

import codesquad.handler.HttpHandler;
import codesquad.http.HttpMethod;
import codesquad.http.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerRegistryTest {

    private HandlerRegistry handlerRegistry;

    private final HttpHandler<String, String> dummyHandler = (req, res, trigger) -> {};
    private final Triggerable<String, String> dummyTriggerable = (req) -> "response";

    @BeforeEach
    void setUp() {
        handlerRegistry = new HandlerRegistry(new ArrayList<>());
    }

    @DisplayName("정상적인 핸들러 등록 및 조회")
    @Test
    void testRegisterAndGetHandler() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test";

        // when
        handlerRegistry.registerHandler(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerRegistry.getHandler(method, Path.of(url)))
                .isNotNull()
                .extracting(HandlerMapping::getHandler, HandlerMapping::getTrigger)
                .doesNotContainNull();
    }

    @DisplayName("없는 경로 조회 시 null 반환")
    @Test
    void testGetHandlerForNonExistentPath() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/nonexistent";

        // when
        HandlerMapping<?, ?> handlerMapping = handlerRegistry.getHandler(method, Path.of(url));

        // then
        assertThat(handlerMapping).isNull();
    }

    @DisplayName("PathVariable 포함된 URL 핸들러 조회")
    @Test
    void testGetHandlerWithPathVariable() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/{id}";

        handlerRegistry.registerHandler(method, url, dummyHandler, dummyTriggerable);

        // when
        HandlerMapping<?, ?> handlerMapping = handlerRegistry.getHandler(method, Path.of("/test/123"));

        // then
        assertThat(handlerMapping)
                .isNotNull()
                .extracting(HandlerMapping::getHandler, HandlerMapping::getTrigger)
                .doesNotContainNull();
    }

    @DisplayName("PathVariable 없는 URL 핸들러 조회")
    @Test
    void testGetHandlerWithoutPathVariable() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test";

        handlerRegistry.registerHandler(method, url, dummyHandler, dummyTriggerable);

        // when
        HandlerMapping<?, ?> handlerMapping = handlerRegistry.getHandler(method, Path.of(url));

        // then
        assertThat(handlerMapping)
                .isNotNull()
                .extracting(HandlerMapping::getHandler, HandlerMapping::getTrigger)
                .doesNotContainNull();
    }

    @DisplayName("다른 HTTP 메서드로 핸들러 조회")
    @Test
    void testGetHandlerWithDifferentHttpMethod() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test";

        handlerRegistry.registerHandler(method, url, dummyHandler, dummyTriggerable);

        // when
        HandlerMapping<?, ?> handlerMapping = handlerRegistry.getHandler(HttpMethod.POST, Path.of(url));

        // then
        assertThat(handlerMapping).isNull();
    }

    @DisplayName("중복된 URL 패턴 핸들러 등록 시 예외 발생")
    @Test
    void testGetHandlerWithDuplicateUrlPattern() {
        // given
        HttpMethod method1 = HttpMethod.GET;
        String url1 = "/test";

        HttpMethod method2 = HttpMethod.GET;
        String url2 = "/test";

        handlerRegistry.registerHandler(method1, url1, dummyHandler, dummyTriggerable);

        // when & then
        assertThatThrownBy(() -> handlerRegistry.registerHandler(method2, url2, dummyHandler, dummyTriggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 등록된 URL입니다.");
    }

    @DisplayName("HandlerMapping: 이상값 테스트 (빈 PathVariable)")
    @Test
    void testHandlerMappingWithEmptyPathVariable() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/{}/details";

        // when & then
        assertThatThrownBy(() -> new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PathVariable이 null이거나 비어있습니다.");
    }

    // 추가 테스트
    @DisplayName("HandlerMapping: 특수문자가 포함된 URL과 매칭")
    @Test
    void testHandlerMappingWithSpecialCharacterMatching() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/{id}";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/!@#")).isTrue();
    }

    @DisplayName("HandlerMapping: 숫자가 포함된 URL과 매칭")
    @Test
    void testHandlerMappingWithNumericMatching() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/{id}";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/123")).isTrue();
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/123/test2")).isFalse();
    }
}