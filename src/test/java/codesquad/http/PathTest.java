package codesquad.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathTest {

    @DisplayName("경로가 null일 경우 예외 발생")
    @Test
    void testNullPath() {
        // given
        String path = null;

        // when & then
        assertThatThrownBy(() -> Path.of(path))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Path는 null이거나 비어있을 수 없습니다.");
    }

    @DisplayName("경로가 비어있는 경우 예외 발생")
    @Test
    void testEmptyPath() {
        // given
        String path = " ";

        // when & then
        assertThatThrownBy(() -> Path.of(path))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Path는 null이거나 비어있을 수 없습니다.");
    }

    @DisplayName("경로가 여러 개의 슬래시로 시작하는 경우 정규화")
    @Test
    void testMultipleSlashes() {
        // given
        String path = "/users//profile///123/";

        // when
        Path result = Path.of(path);

        // then
        assertEquals("/users/profile/123", result.getValue());
        assertEquals(List.of("users", "profile", "123"), result.getSegments());
    }

    @DisplayName("경로가 슬래시로 끝나는 경우 정규화")
    @Test
    void testTrailingSlash() {
        // given
        String path = "/users/profile/123/";

        // when
        Path result = Path.of(path);

        // then
        assertEquals("/users/profile/123", result.getValue());
    }

    @DisplayName("경로가 슬래시로 시작하지 않는 경우 정규화")
    @Test
    void testLeadingSlash() {
        // given
        String path = "users/profile/123";

        // when
        Path result = Path.of(path);

        // then
        assertEquals("/users/profile/123", result.getValue());
    }

    @DisplayName("경로의 세그먼트 파싱")
    @Test
    void testSegmentsParsing() {
        // given
        String path = "/users/profile/123";

        // when
        Path result = Path.of(path);

        // then
        assertEquals(List.of("users", "profile", "123"), result.getSegments());
    }

    @DisplayName("쿼리 파라미터 파싱")
    @Test
    void testQueryParametersParsing() {
        // given
        String path = "/users/profile/123?name=john&age=30";

        // when
        Path result = Path.of(path);

        // then
        assertEquals(Map.of("name", "john", "age", "30"), result.getQueryParameters());
    }

    @DisplayName("쿼리 파라미터가 없는 경우 빈 맵 반환")
    @Test
    void testEmptyQueryParameters() {
        // given
        String path = "/users/profile/123";

        // when
        Path result = Path.of(path);

        // then
        assertTrue(result.getQueryParameters().isEmpty());
    }

    @DisplayName("쿼리 파라미터 디코딩")
    @Test
    void testQueryParametersDecoding() {
        // given
        String path = "/users/profile/123?name=John%20Doe&age=30";

        // when
        Path result = Path.of(path);

        // then
        assertEquals(Map.of("name", "John Doe", "age", "30"), result.getQueryParameters());
    }

    @DisplayName("경로의 기본적인 유효성 검사")
    @Test
    void testValidPath() {
        // given
        String path = "/users/profile/123";

        // when
        Path result = Path.of(path);

        // then
        assertEquals("/users/profile/123", result.getValue());
    }

    @DisplayName("특수 문자 포함 경로의 유효성 검사")
    @Test
    void testPathWithSpecialCharacters() {
        // given
        String path = "/users/prof ile/12 3/";

        // when
        Path result = Path.of(path);

        // then
        assertEquals("/users/prof ile/12 3", result.getValue());
    }

    @DisplayName("쿼리 파라미터가 없는 경로의 유효성 검사")
    @Test
    void testPathWithoutQueryParameters() {
        // given
        String path = "/users/profile/123";

        // when
        Path result = Path.of(path);

        // then
        assertEquals("/users/profile/123", result.getValue());
        assertTrue(result.getQueryParameters().isEmpty());
    }

    @DisplayName("한 개의 쿼리 파라미터가 있는 경로의 유효성 검사")
    @Test
    void testPathWithSingleQueryParameter() {
        // given
        String path = "/users/profile/123?name=john";

        // when
        Path result = Path.of(path);

        // then
        assertEquals(Map.of("name", "john"), result.getQueryParameters());
    }

    @DisplayName("잘못된 쿼리 파라미터 형식의 경로 처리")
    @Test
    void testPathWithMalformedQueryParameter() {
        // given
        String path = "/users/profile/123?name=john&age";

        // when
        Path result = Path.of(path);

        // then
        assertTrue(result.getQueryParameters().containsKey("name"));
        assertTrue(result.getQueryParameters().containsKey("age"));
    }

    @DisplayName("복수의 쿼리 파라미터가 있는 경로의 유효성 검사")
    @Test
    void testPathWithMultipleQueryParameters() {
        // given
        String path = "/users/profile/123?name=john&age=30&city=New%20York";

        // when
        Path result = Path.of(path);

        // then
        assertEquals(Map.of("name", "john", "age", "30", "city", "New York"), result.getQueryParameters());
    }

    @DisplayName("경로에 포함된 유니코드 문자의 처리")
    @Test
    void testPathWithUnicodeCharacters() {
        // given
        String path = "/users/프로필/123";

        // when
        Path result = Path.of(path);

        // then
        assertEquals("/users/프로필/123", result.getValue());
        assertEquals(List.of("users", "프로필", "123"), result.getSegments());
    }

    @DisplayName("경로에 포함된 공백 문자의 처리")
    @Test
    void testPathWithSpaces() {
        // given
        String path = "/users/pro file/123";

        // when
        Path result = Path.of(path);

        // then
        assertEquals("/users/pro file/123", result.getValue());
        assertEquals(List.of("users", "pro file", "123"), result.getSegments());
    }

    @DisplayName("복잡한 쿼리 파라미터가 있는 경로의 유효성 검사")
    @Test
    void testComplexQueryParameters() {
        // given
        String path = "/search?query=John+Doe&sort=desc&filters=name,age,location";

        // when
        Path result = Path.of(path);

        // then
        assertEquals(Map.of("query", "John Doe", "sort", "desc", "filters", "name,age,location"), result.getQueryParameters());
    }

    @DisplayName("잘못된 URL 인코딩 쿼리 파라미터 처리")
    @Test
    void testMalformedURLEncodingInQueryParameters() {
        // given
        String path = "/users/profile/123?name=John%2";

        // when & then
        assertThatThrownBy(() -> Path.of(path))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("쿼리 파라미터를 디코딩하는 중 오류가 발생했습니다.");
    }

    @DisplayName("빈 쿼리 파라미터 처리")
    @Test
    void testEmptyQueryParameter() {
        // given
        String path = "/users/profile/123?name=&age=30";

        // when
        Path result = Path.of(path);

        // then
        assertEquals(Map.of("name", "", "age", "30"), result.getQueryParameters());
    }
}
