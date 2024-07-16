package codesquad.webserver.http.header;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HttpHeadersTest {

    @DisplayName("MultiPart 요청이 왔을 때, Content-Type 헤더의 boundary를 반환한다.")
    @Test
    void getMultipartBoundary() {
        // given
        HttpHeaders httpHeaders = HttpHeaders.emptyHeader();
        httpHeaders.addHeader(HeaderConstants.CONTENT_TYPE, "multipart/form-data");
        httpHeaders.addHeader(HeaderConstants.CONTENT_TYPE, "boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

        // when
        Optional<String> maybeMultipartBoundary = httpHeaders.getMultipartBoundary();

        // then
        assertThat(maybeMultipartBoundary).isPresent()
                .get()
                .isEqualTo("----WebKitFormBoundary7MA4YWxkTrZu0gW");
    }

}