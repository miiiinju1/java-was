package codesquad.handler;

import codesquad.factory.TestHttpRequestFactory;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpVersion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceHandlerTest {

    @DisplayName("static 파일을 읽어온다.")
    @Test
    void readFileAsStream() throws Exception {
        // given
        ResourceHandler resourceHandler = new ResourceHandler();
        String filePath = "/readStaticFileOf.txt";
        HttpRequest request = TestHttpRequestFactory.createGetResourceRequest(filePath);
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);

        // when
        resourceHandler.handle(request, response);

        // then
        String result = response.getBody().toString();

        assertThat(result)
                .contains("example");
    }

    @DisplayName("없는 static 파일을 읽어올 때 예외를 던진다.")
    @Test
    void readFileAsStream_notFound() {
        // given
        ResourceHandler resourceHandler = new ResourceHandler();
        String filePath = "/invalid.txt";
        HttpRequest request = TestHttpRequestFactory.createGetResourceRequest(filePath);

        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);


        // when & then
        assertThatThrownBy(() -> resourceHandler.handle(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File not found! : static/invalid.txt");
    }

}