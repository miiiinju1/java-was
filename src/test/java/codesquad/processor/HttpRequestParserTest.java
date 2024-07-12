package codesquad.processor;

import codesquad.http.HttpMethod;
import codesquad.http.HttpRequest;
import codesquad.http.HttpVersion;
import codesquad.http.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpRequestParserTest {

    private final HttpRequestParser httpRequestParser = new HttpRequestParser();

    @DisplayName("urlencoded된 요청이 오는 경우에는 decode된 body를 HttpRequest에 저장한다.")
    @Test
    void urlencoded() throws IOException {
        // given
        String request = """
                POST /user/create HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Content-Length: 93
                Content-Type: application/x-www-form-urlencoded
                
                userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
                """;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.getBytes());

        // when
        HttpRequest httpRequest = httpRequestParser.parseRequest(byteArrayInputStream);

        // then
        assertThat(httpRequest.getBody())
                .isEqualTo("userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net");
    }

    @DisplayName("urlencoded되어있지 않을 경우 decode하지 않은 body를 HttpRequest에 저장한다.")
    @Test
    void urlencode되어있지_않은_경우() throws IOException {
        // given
        String request = """
                POST /user/create HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Content-Length: 93
                Content-Type: application/json
                
                userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
                """;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.getBytes());


        // when
        HttpRequest httpRequest = httpRequestParser.parseRequest(byteArrayInputStream);

        // then
        assertThat(httpRequest.getBody())
                .isEqualTo("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
    }

    @DisplayName("빈 요청 라인 파싱 시 예외 발생")
    @Test
    void parseRequest_emptyRequestLine() {
        // given
        String rawRequest = "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());


        // when & then
        assertThatThrownBy(() -> httpRequestParser.parseRequest(inputStream))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Request line is null");
    }

    @DisplayName("정상적인 요청 파싱")
    @Test
    void parseRequest_validRequest() throws Exception {
        // given
        String rawRequest = "GET /test HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Content-Length: 0\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());


        // when
        HttpRequest request = httpRequestParser.parseRequest(inputStream);

        // then
        assertThat(request).isNotNull()
                .extracting(HttpRequest::getMethod, HttpRequest::getPath, HttpRequest::getVersion, HttpRequest::getBody)
                .containsExactly(HttpMethod.GET, Path.of("/test"), HttpVersion.HTTP_1_1, "");
        assertThat(request.getHeaders().getHeader("Host")).containsExactly("localhost");
    }

    @DisplayName("유효하지 않은 요청 라인 파싱 시 예외 발생")
    @Test
    void parseRequest_invalidRequestLine() {
        // given
        String rawRequest = "INVALID_REQUEST_LINE\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());


        // when & then
        assertThatThrownBy(() -> httpRequestParser.parseRequest(inputStream))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid request line: INVALID_REQUEST_LINE");
    }

    @DisplayName("유효하지 않은 헤더 라인 파싱 시 예외 발생")
    @Test
    void parseRequest_invalidHeaderLine() {
        // given
        String rawRequest = "GET /test HTTP/1.1\r\n" +
                "Invalid-Header\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());


        // when & then
        assertThatThrownBy(() -> httpRequestParser.parseRequest(inputStream))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid header line: Invalid-Header");
    }

    @DisplayName("유효하지 않은 Content-Length 값 파싱 시 예외 발생")
    @Test
    void parseRequest_invalidContentLength() {
        // given
        String rawRequest = "GET /test HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Content-Length: invalid\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());


        // when & then
        assertThatThrownBy(() -> httpRequestParser.parseRequest(inputStream))
                .isInstanceOf(NumberFormatException.class);
    }

    @DisplayName("유효하지 않은 Content-Type 값 파싱 시 예외 발생")
    @Test
    void parseRequest_invalidContentType() throws Exception {
        // given
        String rawRequest = "POST /test HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Content-Length: 27\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                "key1=value1&key2=invalid%XX";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());


        // when & then
        assertThatThrownBy(() -> httpRequestParser.parseRequest(inputStream))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인코딩 에러가 발생했습니다.");
    }
    @DisplayName("헤더가 없는 요청 파싱")
    @Test
    void parseRequest_noHeaders() throws Exception {
        // given
        String rawRequest = "GET /test HTTP/1.1\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());


        // when
        HttpRequest request = httpRequestParser.parseRequest(inputStream);

        // then
        assertThat(request).isNotNull()
                .extracting(HttpRequest::getMethod, HttpRequest::getPath, HttpRequest::getVersion, HttpRequest::getBody)
                .containsExactly(HttpMethod.GET, Path.of("/test"), HttpVersion.HTTP_1_1, "");
        assertThat(request.getHeaders().getValues()).isEmpty();
    }





}