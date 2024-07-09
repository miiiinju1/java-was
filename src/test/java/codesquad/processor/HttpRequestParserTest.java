package codesquad.processor;

import codesquad.http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestParserTest {

    @DisplayName("urlencoded된 요청이 오는 경우에는 decode된 body를 HttpRequest에 저장한다.")
    @Test
    void urlencoded() throws IOException {
        // given
        HttpRequestParser httpRequestParser = new HttpRequestParser();
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
        HttpRequestParser httpRequestParser = new HttpRequestParser();
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

}