package codesquad.processor.argumentresolver;

import codesquad.http.HttpRequest;
import codesquad.web.user.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginArgumentResolverTest {

    private LoginArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new LoginArgumentResolver();
    }

    @DisplayName("Resolve: 정상적인 요청 파라미터로 LoginRequest 객체 생성")
    @Test
    void resolveWithValidParameters() {
        // given
        String body = "userId=test@example.com&password=secret";
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/login")
                .version("HTTP/1.1")
                .headers(new HashMap<>())
                .body(new ByteArrayInputStream(body.getBytes()))
                .build();

        // when
        LoginRequest loginRequest = resolver.resolve(request);

        // then
        assertThat(loginRequest)
                .isNotNull()
                .extracting(LoginRequest::getUserId, LoginRequest::getPassword)
                .containsExactly("test@example.com", "secret");
    }

    @DisplayName("Resolve: 이메일 누락 시 예외 발생")
    @Test
    void resolveWithMissingEmail() {
        // given
        String body = "password=secret";
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/login")
                .version("HTTP/1.1")
                .headers(new HashMap<>())
                .body(new ByteArrayInputStream(body.getBytes()))
                .build();

        // when & then
        assertThatThrownBy(() -> resolver.resolve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("필수 파라미터가 누락되었습니다.");
    }

    @DisplayName("Resolve: 비밀번호 누락 시 예외 발생")
    @Test
    void resolveWithMissingPassword() {
        // given
        String body = "email=test@example.com";
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/login")
                .version("HTTP/1.1")
                .headers(new HashMap<>())
                .body(new ByteArrayInputStream(body.getBytes()))
                .build();

        // when & then
        assertThatThrownBy(() -> resolver.resolve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("필수 파라미터가 누락되었습니다.");
    }

    @DisplayName("Resolve: 이메일과 비밀번호 둘 다 누락 시 예외 발생")
    @Test
    void resolveWithMissingEmailAndPassword() {
        // given
        String body = "";
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/login")
                .version("HTTP/1.1")
                .headers(new HashMap<>())
                .body(new ByteArrayInputStream(body.getBytes()))
                .build();

        // when & then
        assertThatThrownBy(() -> resolver.resolve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("필수 파라미터가 누락되었습니다.");
    }
}
