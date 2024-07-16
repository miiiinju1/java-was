package codesquad.application.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JsonDeserializerTest {

    @DisplayName("단순한 JSON 문자열을 Map으로 변환")
    @Test
    void testSimpleConvertJsonToMap() {
        // given
        String jsonString = "{\"key1\":\"value1\",\"key2\":\"value2\"}";

        // when
        Map<String, String> result = JsonDeserializer.simpleConvertJsonToMap(jsonString);

        // then
        assertThat(result).isNotNull()
                .hasSize(2)
                .containsEntry("key1", "value1")
                .containsEntry("key2", "value2");
    }

    @DisplayName("공백이 있는 JSON 문자열을 Map으로 변환")
    @Test
    void testSimpleConvertJsonToMap_WithSpaces() {
        // given
        String jsonString = "{ \"key1\" : \"value1\" , \"key2\" : \"value2\" }";

        // when
        Map<String, String> result = JsonDeserializer.simpleConvertJsonToMap(jsonString);

        // then
        assertThat(result).isNotNull()
                .hasSize(2)
                .containsEntry("key1", "value1")
                .containsEntry("key2", "value2");
    }

    @DisplayName("빈 JSON 문자열을 Map으로 변환")
    @Test
    void testSimpleConvertJsonToMap_Empty() {
        // given
        String jsonString = "{}";

        // when & then
        assertThatThrownBy(() -> JsonDeserializer.simpleConvertJsonToMap(jsonString))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid JSON format");
    }

    @DisplayName("JSON 문자열에 중복된 키가 있을 때")
    @Test
    void testSimpleConvertJsonToMap_DuplicateKeys() {
        // given
        String jsonString = "{\"key1\":\"value1\",\"key1\":\"value2\"}";

        // when
        Map<String, String> result = JsonDeserializer.simpleConvertJsonToMap(jsonString);

        // then
        assertThat(result).isNotNull()
                .hasSize(1)
                .containsEntry("key1", "value2");
    }

    @DisplayName("입력이 null일 때 예외 발생")
    @Test
    void testSimpleConvertJsonToMap_NullInput() {
        // given, when, then
        assertThatThrownBy(() -> JsonDeserializer.simpleConvertJsonToMap(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Input string is null");
    }
}
