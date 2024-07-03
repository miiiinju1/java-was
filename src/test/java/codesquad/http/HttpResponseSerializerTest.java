package codesquad.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class HttpResponseSerializerTest {

    @DisplayName("HttpResponse 객체를 byte 배열로 변환한다.")
    @Test
    void buildHttpResponse() throws IOException {
        // given
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        String bodyStr = "<html><body>Hello World!</body></html>";
        byte[] bodyBytes = bodyStr.getBytes();

        HttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_1_1);
        httpResponse.getHttpHeaders().addHeader("Content-Type", "text/html");
        httpResponse.getBody().write(bodyBytes);


        // when
        byte[] result = httpResponseSerializer.buildHttpResponse(httpResponse);

        // then
        String resultStr = new String(result);
        assertThat(resultStr)
                .isNotNull()
                .contains("Content-Type: text/html")
                .contains(bodyStr);
    }

    @DisplayName("HttpResponse 객체를 byte 배열로 변환한다.")
    @Test
    void buildHttpResponse2() throws IOException {
        // given
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        String path = "/index.html";


        HttpResponse httpResponse = HttpResponse.notFoundOf(path);

        // when
        byte[] result = httpResponseSerializer.buildHttpResponse(httpResponse);

        // then
        String resultStr = new String(result);
        assertThat(resultStr)
                .isNotNull()
                .contains("HTTP/1.1 404 Not Found")
                .contains("Content-Type: text/html; charset=UTF-8")
                .contains("<html><body><h1>404 Not Found " + path + "</h1></body></html>");
    }

}