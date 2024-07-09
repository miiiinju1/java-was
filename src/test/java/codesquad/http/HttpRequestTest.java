package codesquad.http;

import codesquad.http.header.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpRequestTest {


    @DisplayName("Builder: 정상적인 HTTP 요청 생성")
    @Test
    void testBuild() {
        // given
        String method = "GET";
        String path = "/";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost");
        String body = "";

        // when
        HttpRequest request = HttpRequest.builder()
                .method(method)
                .path(path)
                .version(version)
                .headers(headers)
                .body(body)
                .build();

        // then
        assertThat(request).isNotNull()
                .extracting(HttpRequest::getMethod, HttpRequest::getPath, HttpRequest::getVersion, HttpRequest::getBody)
                .doesNotContainNull()
                .containsExactly(HttpMethod.GET, Path.of("/"), HttpVersion.HTTP_1_1, body);
    }

    @DisplayName("Builder: 메서드가 null일 때 예외 발생")
    @Test
    void testBuildWithNullMethod() {
        // given
        String method = null;
        String path = "/";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost");
        String body = "";

        // when & then
        assertThatThrownBy(() ->  HttpRequest.builder()
                .method(method)
                .path(path)
                .version(version)
                .headers(headers)
                .body(body)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("HTTP Method cannot be null");
    }

    @DisplayName("Builder: 메서드가 이상할 떄 예외 발생")
    @Test
    void testBuildWithInvalidMethod() {
        // given
        String method = "invalid";
        String path = "/";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost");
        String body = "";

        // when & then
        assertThatThrownBy(() ->  HttpRequest.builder()
                .method(method)
                .path(path)
                .version(version)
                .headers(headers)
                .body(body)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid HTTP Method : invalid");
    }

    @DisplayName("Builder: 경로가 null일 때 예외 발생")
    @Test
    void testBuildWithNullPath() {
        // given
        String method = "GET";
        String path = null;
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost");
        String body = "";

        // when & then
        assertThatThrownBy(() -> HttpRequest.builder()
                .method(method)
                .path(path)
                .version(version)
                .headers(headers)
                .body(body)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Path는 null이거나 비어있을 수 없습니다.");
    }

    @DisplayName("Builder: 버전이 null일 때 예외 발생")
    @Test
    void testBuildWithNullVersion() {
        // given
        String method = "GET";
        String path = "/";
        String version = null;
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost");
        String body = "";

        // when & then
        assertThatThrownBy(() -> HttpRequest.builder()
                .method(method)
                .path(path)
                .version(version)
                .headers(headers)
                .body(body)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("HTTP Version cannot be null");
    }

    @DisplayName("Builder: 정상적인 헤더 설정")
    @Test
    void testBuildWithHeaders() {
        // given
        String method = "GET";
        String path = "/";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost");
        String body = "";

        // when
        HttpRequest request = HttpRequest.builder()
                .method(method)
                .path(path)
                .version(version)
                .headers(headers)
                .body(body)
                .build();

        // then
        HttpHeaders requestHeaders = request.getHeaders();
        assertThat(requestHeaders.getHeader("Host"))
                .isEqualTo("localhost");
    }

    @DisplayName("Builder: 정상적인 바디 설정")
    @Test
    void testBuildWithBody() {
        // given
        String method = "POST";
        String path = "/";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost");
        String body = "test body";

        // when
        HttpRequest request = HttpRequest.builder()
                .method(method)
                .path(path)
                .version(version)
                .headers(headers)
                .body(body)
                .build();

        // then
        assertThat(request).isNotNull()
                .extracting(HttpRequest::getBody)
                .isEqualTo(body);
    }

    @DisplayName("Attributes: 정상적으로 속성 설정 및 조회")
    @Test
    void testSetAndGetAttributes() {
        // given
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/")
                .version("HTTP/1.1")
                .headers(new HashMap<>())
                .body("")
                .build();
        String attributeName = "testAttribute";
        String attributeValue = "value";

        // when
        request.setAttributes(attributeName, attributeValue);
        Optional<Object> value = request.getAttributes(attributeName);

        // then
        assertThat(value)
                .isPresent()
                .get()
                .isEqualTo(attributeValue);
    }

    @DisplayName("Attributes: 존재하지 않는 속성 조회 시 null 반환")
    @Test
    void testGetNonExistentAttributes() {
        // given
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/")
                .version("HTTP/1.1")
                .headers(new HashMap<>())
                .body("")
                .build();
        String attributeName = "nonExistentAttribute";

        // when
        Optional<Object> value = request.getAttributes(attributeName);

        // then
        assertThat(value)
                .isNotPresent();
    }

    @DisplayName("Attributes: null 속성 이름으로 설정 시 예외 발생")
    @Test
    void testSetAttributesWithNullName() {
        // given
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/")
                .version("HTTP/1.1")
                .headers(new HashMap<>())
                .body("")
                .build();

        String attributeValue = "value";

        // when & then
        assertThatThrownBy(() -> request.setAttributes(null, attributeValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("키는 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("Attributes: null 속성 값으로 설정 시 예외 발생")
    @Test
    void testSetAttributesWithNullValue() {
        // given
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/")
                .version("HTTP/1.1")
                .headers(new HashMap<>())
                .body("")
                .build();
        String attributeName = "testAttribute";

        // when & then
        assertThatThrownBy(() -> request.setAttributes(attributeName, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("값은 null일 수 없습니다.");
    }

}