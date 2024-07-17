package codesquad.webserver.helper;

import codesquad.webserver.helper.MultiPartParseHelper.MultiPart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MultiPartParseHelperTest {

    private static final String BOUNDARY = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

    @DisplayName("parse: 멀티파트 폼 데이터를 정상적으로 파싱한다.")
    @Test
    void parseMultipartData() throws IOException {
        // given
        String multipartData = "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"text\"\r\n\r\n" +
                "text default\r\n" +
                "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"example.txt\"\r\n" +
                "Content-Type: text/plain\r\n\r\n" +
                "This is a file content\r\n" +
                "--" + BOUNDARY + "--";
        InputStream inputStream = new ByteArrayInputStream(multipartData.getBytes(StandardCharsets.UTF_8));

        // when
        Map<String, MultiPart> parts = MultiPartParseHelper.parse(inputStream, BOUNDARY);

        // then
        assertThat(parts).isNotNull().containsKeys("text", "file");

        MultiPart textPart = parts.get("text");
        assertThat(textPart).isNotNull()
                .extracting(MultiPart::getName, MultiPart::getFilename, MultiPart::getContentType, MultiPart::getTextContent)
                .containsExactly("text", null, null, "text default");

        MultiPart filePart = parts.get("file");
        assertThat(filePart).isNotNull()
                .extracting(MultiPart::getName, MultiPart::getFilename, MultiPart::getContentType, MultiPart::getTextContent)
                .containsExactly("file", "example.txt", "text/plain", "This is a file content");
    }

    @DisplayName("parse: 잘못된 형식의 멀티파트 데이터가 주어지면 예외를 던진다.")
    @Test
    void parseInvalidMultipartData() {
        // given
        String dataWithoutHeaders = "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"text\"\r\n" +
                "text default\r\n" +
                "--" + BOUNDARY + "--";
        InputStream inputStream = new ByteArrayInputStream(dataWithoutHeaders.getBytes(StandardCharsets.UTF_8));

        // when & then
        assertThatThrownBy(() -> MultiPartParseHelper.parse(inputStream, BOUNDARY))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Invalid part data: missing headers");
    }

    @DisplayName("parse: filename이 없는 멀티파트 데이터를 처리한다.")
    @Test
    void parseMultipartDataWithoutFilename() throws IOException {
        // given
        String multipartDataWithoutFilename = "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"text\"\r\n\r\n" +
                "text default\r\n" +
                "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"\r\n" +
                "Content-Type: text/plain\r\n\r\n" +
                "This is a file content\r\n" +
                "--" + BOUNDARY + "--";
        InputStream inputStream = new ByteArrayInputStream(multipartDataWithoutFilename.getBytes(StandardCharsets.UTF_8));

        // when
        Map<String, MultiPart> parts = MultiPartParseHelper.parse(inputStream, BOUNDARY);

        // then
        assertThat(parts).isNotNull().containsKeys("text", "file");

        MultiPart textPart = parts.get("text");
        assertThat(textPart).isNotNull()
                .extracting(MultiPart::getName, MultiPart::getFilename, MultiPart::getContentType, MultiPart::getTextContent)
                .containsExactly("text", null, null, "text default");

        MultiPart filePart = parts.get("file");
        assertThat(filePart).isNotNull()
                .extracting(MultiPart::getName, MultiPart::getFilename, MultiPart::getContentType, MultiPart::getTextContent)
                .containsExactly("file", null, "text/plain", "This is a file content");
    }

    @DisplayName("parse: Content-Type이 없는 멀티파트 데이터를 처리한다.")
    @Test
    void parseMultipartDataWithoutContentType() throws IOException {
        // given
        String multipartDataWithoutContentType = "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"text\"\r\n\r\n" +
                "text default\r\n" +
                "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"example.txt\"\r\n\r\n" +
                "This is a file content\r\n" +
                "--" + BOUNDARY + "--";
        InputStream inputStream = new ByteArrayInputStream(multipartDataWithoutContentType.getBytes(StandardCharsets.UTF_8));

        // when
        Map<String, MultiPart> parts = MultiPartParseHelper.parse(inputStream, BOUNDARY);

        // then
        assertThat(parts).isNotNull().containsKeys("text", "file");

        MultiPart textPart = parts.get("text");
        assertThat(textPart).isNotNull()
                .extracting(MultiPart::getName, MultiPart::getFilename, MultiPart::getContentType, MultiPart::getTextContent)
                .containsExactly("text", null, null, "text default");

        MultiPart filePart = parts.get("file");
        assertThat(filePart).isNotNull()
                .extracting(MultiPart::getName, MultiPart::getFilename, MultiPart::getContentType, MultiPart::getTextContent)
                .containsExactly("file", "example.txt", null, "This is a file content");
    }
}
