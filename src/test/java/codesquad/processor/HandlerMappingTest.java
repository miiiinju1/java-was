package codesquad.processor;

import codesquad.handler.HttpHandler;
import codesquad.http.HttpMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerMappingTest {

    private HttpHandler<String, String> dummyHandler = (req, res, trigger) -> {};
    private Triggerable<String, String> dummyTriggerable = (req) -> "response";

    @DisplayName("HandlerMapping: 정상적인 생성")
    @Test
    void testHandlerMappingCreation() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping).isNotNull()
                .extracting(HandlerMapping::getHandler, HandlerMapping::getTrigger)
                .doesNotContainNull();
    }

    @DisplayName("HandlerMapping: HttpMethod가 null일 때 예외 발생")
    @Test
    void testHandlerMappingWithNullHttpMethod() {
        // given
        HttpMethod method = null;
        String url = "/test";

        // when & then
        assertThatThrownBy(() -> new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("httpMethod이 null입니다.");
    }

    @DisplayName("HandlerMapping: URL이 null일 때 예외 발생")
    @Test
    void testHandlerMappingWithNullUrl() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = null;

        // when & then
        assertThatThrownBy(() -> new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("url이 null이거나 비어있습니다.");
    }

    @DisplayName("HandlerMapping: URL이 빈 문자열일 때 예외 발생")
    @Test
    void testHandlerMappingWithEmptyUrl() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "";

        // when & then
        assertThatThrownBy(() -> new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("url이 null이거나 비어있습니다.");
    }

    @DisplayName("HandlerMapping: Handler가 null일 때 예외 발생")
    @Test
    void testHandlerMappingWithNullHandler() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test";
        HttpHandler<String, String> handler = null;

        // when & then
        assertThatThrownBy(() -> new HandlerMapping<>(method, url, handler, dummyTriggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("handler가 null입니다.");
    }

    @DisplayName("HandlerMapping: Triggerable이 null일 때 예외 발생")
    @Test
    void testHandlerMappingWithNullTriggerable() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test";
        Triggerable<String, String> triggerable = null;

        // when & then
        assertThatThrownBy(() -> new HandlerMapping<>(method, url, dummyHandler, triggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("triggerable이 null입니다.");
    }

    @DisplayName("HandlerMapping: PathVariable이 포함된 URL 패턴")
    @Test
    void testHandlerMappingWithPathVariable() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/{id}";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping).isNotNull();
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/123")).isTrue();
    }

    @DisplayName("HandlerMapping: PathVariable이 없는 URL 패턴")
    @Test
    void testHandlerMappingWithoutPathVariable() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping).isNotNull();
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test")).isTrue();
    }

    @DisplayName("HandlerMapping: URL 패턴 불일치")
    @Test
    void testHandlerMappingUrlMismatch() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/notTest")).isFalse();
    }

    @DisplayName("HandlerMapping: 다른 HttpMethod 불일치")
    @Test
    void testHandlerMappingMethodMismatch() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.POST, "/test")).isFalse();
    }

    @DisplayName("HandlerMapping: POST 메서드와 정상적인 URL 패턴")
    @Test
    void testHandlerMappingWithPostMethod() {
        // given
        HttpMethod method = HttpMethod.POST;
        String url = "/test";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.POST, "/test")).isTrue();
    }

    @DisplayName("HandlerMapping: PUT 메서드와 PathVariable URL 패턴")
    @Test
    void testHandlerMappingWithPutMethodAndPathVariable() {
        // given
        HttpMethod method = HttpMethod.PUT;
        String url = "/test/{id}";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.PUT, "/test/123")).isTrue();
    }

    @DisplayName("HandlerMapping: DELETE 메서드와 PathVariable URL 패턴")
    @Test
    void testHandlerMappingWithDeleteMethodAndPathVariable() {
        // given
        HttpMethod method = HttpMethod.DELETE;
        String url = "/test/{id}";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.DELETE, "/test/123")).isTrue();
    }

    @DisplayName("HandlerMapping: URL 패턴 중복")
    @Test
    void testHandlerMappingWithDuplicateUrlPattern() {
        // given
        HttpMethod method1 = HttpMethod.GET;
        String url1 = "/test";
        HttpMethod method2 = HttpMethod.GET;
        String url2 = "/test";

        // when
        HandlerMapping<String, String> handlerMapping1 = new HandlerMapping<>(method1, url1, dummyHandler, dummyTriggerable);
        HandlerMapping<String, String> handlerMapping2 = new HandlerMapping<>(method2, url2, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping1.matchRequest(HttpMethod.GET, "/test")).isTrue();
        assertThat(handlerMapping2.matchRequest(HttpMethod.GET, "/test")).isTrue();
    }

    @DisplayName("HandlerMapping: URL 패턴과 PathVariable 중복")
    @Test
    void testHandlerMappingWithDuplicateUrlPatternAndPathVariable() {
        // given
        HttpMethod method1 = HttpMethod.GET;
        String url1 = "/test/{id}";
        HttpMethod method2 = HttpMethod.GET;
        String url2 = "/test/{id}";

        // when
        HandlerMapping<String, String> handlerMapping1 = new HandlerMapping<>(method1, url1, dummyHandler, dummyTriggerable);
        HandlerMapping<String, String> handlerMapping2 = new HandlerMapping<>(method2, url2, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping1.matchRequest(HttpMethod.GET, "/test/123")).isTrue();
        assertThat(handlerMapping2.matchRequest(HttpMethod.GET, "/test/123")).isTrue();
    }

    @DisplayName("HandlerMapping: URL에 특수문자 포함")
    @Test
    void testHandlerMappingWithSpecialCharacters() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/!@#$%^&*()";

        // when & then
        assertThatThrownBy(() -> new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("url에 허용되지 않은 특수 문자가 포함될 수 없습니다.");
    }

    @DisplayName("HandlerMapping: URL에 공백 포함")
    @Test
    void testHandlerMappingWithSpaces() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/with space";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/with space")).isTrue();
    }

    @DisplayName("HandlerMapping: URL 대소문자 구분")
    @Test
    void testHandlerMappingCaseSensitivity() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/CaseSensitive";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/CaseSensitive")).isTrue();
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/casesensitive")).isFalse();
    }

    @DisplayName("HandlerMapping: URL 끝에 슬래시 포함")
    @Test
    void testHandlerMappingWithTrailingSlash() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/")).isTrue();
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test")).isFalse();
    }

    @DisplayName("HandlerMapping: URL 중간에 슬래시 두 개 포함")
    @Test
    void testHandlerMappingWithDoubleSlash() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test//double";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test//double")).isTrue();
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/double")).isFalse();
    }

    @DisplayName("HandlerMapping: URL 패턴과 PathVariable 포함하여 여러 레벨")
    @Test
    void testHandlerMappingWithMultipleLevelsAndPathVariable() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/{id}/details";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/123/details")).isTrue();
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/123/22/details")).isFalse();
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test/123")).isFalse();
    }

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

    @DisplayName("HandlerMapping: URL에 언더스코어 포함")
    @Test
    void testHandlerMappingWithUnderscore() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test_with_underscore";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test_with_underscore")).isTrue();
    }

    @DisplayName("HandlerMapping: URL에 하이픈 포함")
    @Test
    void testHandlerMappingWithHyphen() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test-with-hyphen";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test-with-hyphen")).isTrue();
    }

    @DisplayName("HandlerMapping: 경계값 테스트 (URL 중복 슬래시)")
    @Test
    void testHandlerMappingBoundaryDoubleSlash() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test//path";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test//path")).isTrue();
    }

    @DisplayName("HandlerMapping: 이상값 테스트 (허용되지 않는 특수문자 포함)")
    @Test
    void testHandlerMappingWithDisallowedSpecialCharacters() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test/!@#$%^&*()";

        // when & then
        assertThatThrownBy(() -> new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("url에 허용되지 않은 특수 문자가 포함될 수 없습니다.");
    }

    @DisplayName("HandlerMapping: 정상값 테스트 (URL에 숫자 포함)")
    @Test
    void testHandlerMappingWithNumbers() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test123";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test123")).isTrue();
    }

    @DisplayName("HandlerMapping: 정상값 테스트 (URL에 점 포함)")
    @Test
    void testHandlerMappingWithDot() {
        // given
        HttpMethod method = HttpMethod.GET;
        String url = "/test.with.dot";

        // when
        HandlerMapping<String, String> handlerMapping = new HandlerMapping<>(method, url, dummyHandler, dummyTriggerable);

        // then
        assertThat(handlerMapping.matchRequest(HttpMethod.GET, "/test.with.dot")).isTrue();
    }

}
