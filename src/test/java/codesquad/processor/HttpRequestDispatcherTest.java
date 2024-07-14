package codesquad.processor;

import codesquad.handler.HttpHandler;
import codesquad.http.HttpMethod;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HttpRequestDispatcherTest {

    private TestHttpHandler defaultHandler;
    private HandlerRegistry handlerRegistry;
    private HttpRequestDispatcher dispatcher;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        defaultHandler = new TestHttpHandler();
        handlerRegistry = new HandlerRegistry(new ArrayList<>());
        dispatcher = new HttpRequestDispatcher(defaultHandler, handlerRegistry);

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Host", List.of("localhost"));
        httpRequest = HttpRequest.builder()
                .method("GET")
                .path("/")
                .version("HTTP/1.1")
                .headers(headers)
                .body(new ByteArrayInputStream("".getBytes()))
                .build();

        httpResponse = new HttpResponse(HttpVersion.HTTP_1_1);
    }

    @DisplayName("handleConnection: 핸들러가 있는 경우")
    @Test
    void handleConnectionWithExistingHandler() {
        // given
        TestHttpHandler handler = new TestHttpHandler();
        TestTriggerable triggerable = new TestTriggerable();
        handlerRegistry.registerHandler(HttpMethod.GET, "/", handler, triggerable);

        // when & then
        assertDoesNotThrow(() -> dispatcher.handleConnection(httpRequest, httpResponse));
        assertThat(handler.isHandled()).isTrue();
        assertThat(defaultHandler.isHandled()).isFalse();
    }

    @DisplayName("handleConnection: 핸들러가 없는 경우")
    @Test
    void handleConnectionWithNoHandler() {
        // when & then
        assertDoesNotThrow(() -> dispatcher.handleConnection(httpRequest, httpResponse));
        assertThat(defaultHandler.isHandled()).isTrue();
    }

    @DisplayName("handleConnection: 핸들러 예외 발생")
    @Test
    void handleConnectionWithHandlerException() {
        // given
        TestHttpHandler handler = new TestHttpHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response, Triggerable<String, String> triggerable) {
                throw new RuntimeException("예외 발생");
            }
        };
        TestTriggerable triggerable = new TestTriggerable();
        handlerRegistry.registerHandler(HttpMethod.GET, "/", handler, triggerable);

        // when & then
        assertThatThrownBy(() -> dispatcher.handleConnection(httpRequest, httpResponse))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("예외 발생");
        assertThat(defaultHandler.isHandled()).isFalse();
    }

    private static class TestHttpHandler implements HttpHandler<String, String> {
        private boolean handled = false;

        @Override
        public void handle(HttpRequest request, HttpResponse response, Triggerable<String, String> triggerable) {
            handled = true;
        }

        public boolean isHandled() {
            return handled;
        }
    }

    private static class TestTriggerable implements Triggerable<String, String> {
        @Override
        public String run(String input) {
            return "response";
        }
    }
}
