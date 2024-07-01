package codesquad.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceHandlerTest {

    @DisplayName("static 파일을 읽어온다.")
    @Test
    void readFileAsStream() {
        // given
        ResourceHandler resourceHandler = new ResourceHandler();
        String filePath = "readStaticFileOf.txt";

        // when
        InputStream inputStream = resourceHandler.readFileAsStream(filePath);

        // then
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        assertThat(br.lines())
                .contains("example");
    }

    @DisplayName("없는 static 파일을 읽어올 때 예외를 던진다.")
    @Test
    void readFileAsStream_notFound() {
        // given
        ResourceHandler resourceHandler = new ResourceHandler();
        String filePath = "invalid.txt";

        // when & then
        assertThatThrownBy(() -> resourceHandler.readFileAsStream(filePath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File not found! : static/invalid.txt");
    }

}