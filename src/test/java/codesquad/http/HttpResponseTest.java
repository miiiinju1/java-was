package codesquad.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpResponseTest {

    @DisplayName("HttpResponse 객체를 생성한다.")
    @Test
    void create() throws IOException {
        // given
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        HttpStatus httpStatus = HttpStatus.OK;
        Map<String, String> headers = Map.of("Content-Type", "text/html; charset=UTF-8");
        String bodyStr = "<html><body>Hello World!</body></html>";
        byte[] bodyBytes = bodyStr.getBytes();
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        body.write(bodyBytes);

        // when
        HttpResponse httpResponse = new HttpResponse(httpVersion, httpStatus, headers, body);

        // then
        assertThat(httpResponse)
                .extracting("httpVersion", "httpStatus", "headers", "body")
                .containsExactly(httpVersion, httpStatus, headers, body);
    }

    @DisplayName("HttpVersion없이 HttpResponse 객체를 생성하면 예외가 발생한다.")
    @Test
    void createWithoutHttpVersion() throws IOException {
        // given
        HttpVersion httpVersion = null;
        HttpStatus httpStatus = HttpStatus.OK;
        Map<String, String> headers = Map.of("Content-Type", "text/html; charset=UTF-8");
        String bodyStr = "<html><body>Hello World!</body></html>";
        byte[] bodyBytes = bodyStr.getBytes();
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        body.write(bodyBytes);

        // when & then
        assertThatThrownBy(() -> new HttpResponse(httpVersion, httpStatus, headers, body))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("HttpVersion이 존재하지 않습니다.");
    }

    @DisplayName("HttpStatus없이 HttpResponse 객체를 생성하면 예외가 발생한다.")
    @Test
    void createWithoutHttpStatus() throws IOException {
        // given
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        HttpStatus httpStatus = null;
        Map<String, String> headers = Map.of("Content-Type", "text/html; charset=UTF-8");
        String bodyStr = "<html><body>Hello World!</body></html>";
        byte[] bodyBytes = bodyStr.getBytes();
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        body.write(bodyBytes);

        // when & then
        assertThatThrownBy(() -> new HttpResponse(httpVersion, httpStatus, headers, body))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("HttpStatus가 존재하지 않습니다.");
    }

    @DisplayName("headers없이 HttpResponse 객체를 생성하면 예외가 발생한다.")
    @Test
    void createWithoutheaders() throws IOException {
        // given
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        HttpStatus httpStatus = HttpStatus.OK;
        Map<String, String> headers = null;
        String bodyStr = "<html><body>Hello World!</body></html>";
        byte[] bodyBytes = bodyStr.getBytes();
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        body.write(bodyBytes);

        // when & then
        assertThatThrownBy(() -> new HttpResponse(httpVersion, httpStatus, headers, body))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Headers가 존재하지 않습니다.");
    }
}