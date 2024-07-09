package codesquad.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class Path {

    private final String basePath;
    private final String query;
    private final List<String> segments;
    private final Map<String, String> queryParameters;

    public static Path of(String fullPath) {
        return new Path(fullPath);
    }

    public Path(String fullPath) {
        validatePath(fullPath);
        String[] pathParts = splitPathAndQuery(fullPath);
        this.basePath = validateAndNormalizePath(pathParts[0]);
        this.query = pathParts[1];
        this.segments = parseSegments(this.basePath);
        this.queryParameters = parseQueryParameters(this.query);
    }

    public String getBasePath() {
        return basePath;
    }

    public String getQuery() {
        return query;
    }

    public List<String> getSegments() {
        return Collections.unmodifiableList(segments);
    }

    public Map<String, String> getQueryParameters() {
        return Collections.unmodifiableMap(queryParameters);
    }

    private String validateAndNormalizePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path는 null이거나 비어있을 수 없습니다.");
        }

        // /가 여러개인 경우 하나로 줄이기
        path = path.trim().replaceAll("/{2,}", "/");

        // /로 시작하지 않는 경우 /를 추가
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        // /로 끝나는 경우 /를 제거
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    private void validatePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path는 null이거나 비어있을 수 없습니다.");
        }
    }

    private String[] splitPathAndQuery(String fullPath) {
        String[] parts = fullPath.split("\\?", 2);
        String basePath = parts[0];
        String queryString = (parts.length > 1) ? parts[1] : "";
        return new String[]{basePath, queryString};
    }

    private List<String> parseSegments(String basePath) {
        List<String> segments = new ArrayList<>();
        String[] parts = basePath.split("/");

        for (String part : parts) {
            if (!part.isEmpty()) {
                segments.add(part);
            }
        }

        return segments;
    }

    private Map<String, String> parseQueryParameters(String queryString) {
        Map<String, String> queryParameters = new HashMap<>();
        if (!queryString.isEmpty()) {
            String[] pairs = queryString.split("&");

            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                try {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], "UTF-8") : "";
                    queryParameters.put(key, value);
                } catch (IllegalArgumentException | UnsupportedEncodingException e) {
                    throw new IllegalArgumentException("쿼리 파라미터를 디코딩하는 중 오류가 발생했습니다.");
                }
            }
        }
        return queryParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(basePath, path.basePath) && Objects.equals(query, path.query) && Objects.equals(segments, path.segments) && Objects.equals(queryParameters, path.queryParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basePath, query, segments, queryParameters);
    }

    @Override
    public String toString() {
        return "Path{" +
                "value='" + basePath + '\'' +
                ", segments=" + segments +
                ", queryParameters=" + queryParameters +
                '}';
    }
}
