package codesquad.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpMethodTest {

    @DisplayName("HttpMethod: 유효한 메서드 변환 테스트")
    @ParameterizedTest(name = "{0} 은/는 HttpMethod enum인 {1} 으로 변환되어야 한다.")
    @MethodSource("provideValidHttpMethods")
    void testHttpMethodOfValidMethods(String method, HttpMethod expected) {
        // when
        HttpMethod httpMethod = HttpMethod.of(method);

        // then
        assertThat(httpMethod).isEqualTo(expected);
    }

    @DisplayName("HttpMethod: 유효하지 않은 메서드 변환 시 예외 발생")
    @ParameterizedTest(name = "{0} 은/는 IllegalArgumentException을 발생시킨다.")
    @MethodSource("provideInvalidHttpMethods")
    void testHttpMethodOfInvalidMethods(String method) {
        // when & then
        assertThatThrownBy(() -> HttpMethod.of(method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid HTTP Method : " + method);
    }

    @DisplayName("HttpMethod: null 메서드 변환 시 예외 발생")
    @Test
    void testHttpMethodOfNullMethod() {
        // when & then
        assertThatThrownBy(() -> HttpMethod.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("HTTP Method cannot be null");
    }

    static Stream<Arguments> provideValidHttpMethods() {
        return Stream.of(
                Arguments.of("GET", HttpMethod.GET),
                Arguments.of("POST", HttpMethod.POST),
                Arguments.of("PUT", HttpMethod.PUT),
                Arguments.of("DELETE", HttpMethod.DELETE),
                Arguments.of("HEAD", HttpMethod.HEAD),
                Arguments.of("OPTIONS", HttpMethod.OPTIONS),
                Arguments.of("PATCH", HttpMethod.PATCH),
                Arguments.of("get", HttpMethod.GET),
                Arguments.of("post", HttpMethod.POST),
                Arguments.of("put", HttpMethod.PUT),
                Arguments.of("delete", HttpMethod.DELETE),
                Arguments.of("head", HttpMethod.HEAD),
                Arguments.of("options", HttpMethod.OPTIONS),
                Arguments.of("patch", HttpMethod.PATCH)
        );
    }

    static Stream<Arguments> provideInvalidHttpMethods() {
        return Stream.of(
                Arguments.of("INVALID"),
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("123")
        );
    }
}
