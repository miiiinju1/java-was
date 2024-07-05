package codesquad.handler;

import codesquad.factory.TestHttpRequestFactory;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpVersion;
import codesquad.processor.Triggerable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceHandlerTest {

    @DisplayName("static 파일을 읽어온다.")
    @Test
    void readFileAsStream() throws Exception {
        // given
        ResourceHandlerAdapter<Void, Void> resourceHandler = new ResourceHandlerAdapter<>();
        String filePath = "/readStaticFileOf.txt";
        HttpRequest request = TestHttpRequestFactory.createGetResourceRequest(filePath);
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        Triggerable<Void, Void> triggerable = o -> null;

        // when
        resourceHandler.handle(request, response, triggerable);

        // then
        String result = response.getBody().toString();

        assertThat(result)
                .contains("example");
    }

    @DisplayName("없는 static 파일을 읽어올 때 예외를 던진다.")
    @Test
    void readFileAsStream_notFound() {
        // given
        ResourceHandlerAdapter<Void, Void> resourceHandler = new ResourceHandlerAdapter<>();
        String filePath = "/invalid.txt";
        HttpRequest request = TestHttpRequestFactory.createGetResourceRequest(filePath);

        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        Triggerable<Void, Void> triggerable = o -> null;


        // when & then
        assertThatThrownBy(() -> resourceHandler.handle(request, response, triggerable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("파일을 찾을 수 없습니다.");
    }

}