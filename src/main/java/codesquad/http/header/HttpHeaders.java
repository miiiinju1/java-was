package codesquad.http.header;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

public class HttpHeaders {

    private final Map<String, List<String>> valueMap;

    public static HttpHeaders of(Map<String, List<String>> headers) {
        return new HttpHeaders(headers);
    }

    public static HttpHeaders emptyHeader() {
        return new HttpHeaders();
    }

    public void addHeader(final String key, final String value) {
        validateHeaderKey(key);
        validateHeaderValue(value);
        valueMap.computeIfAbsent(key, k -> new ArrayList<>()).add(value.trim());
    }

    public boolean containsHeader(final String key) {
        return valueMap.containsKey(key);
    }

    public List<String> getHeader(final String key) {
        List<String> values = valueMap.get(key);
        if (values == null) {
            throw new IllegalArgumentException("Header not found: " + key);
        }
        return values;
    }

    public String getSubValueOfHeader(final String key, final String subkey) {
        List<String> values = valueMap.get(key);
        if (values == null) {
            throw new IllegalArgumentException("Header not found: " + key);
        }
        for (String value : values) {
            String[] keyValue = value.split("=");
            if (keyValue[0].equals(subkey)) {
                return keyValue[1];
            }
        }
        throw new IllegalArgumentException("Subkey not found: " + subkey);
    }

    public Set<Map.Entry<String, List<String>>> getValues() {
        return valueMap.entrySet();
    }

    private void validateHeaderKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Header key cannot be null or empty");
        }
    }

    private void validateHeaderValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Header value cannot be null");
        }
    }

    private HttpHeaders(Map<String, List<String>> valueMap) {
        this.valueMap = new HashMap<>(valueMap);
    }

    private HttpHeaders() {
        this.valueMap = new HashMap<>();
    }
}
