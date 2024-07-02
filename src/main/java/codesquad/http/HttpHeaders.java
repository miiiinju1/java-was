package codesquad.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpHeaders {

    private final Map<String, String> headerMap;

    public static HttpHeaders of(Map<String, String> headers) {
        return new HttpHeaders(headers);
    }

    public void addHeader(String key, String value) {
        headerMap.put(key, value);
    }

    public String getHeader(String key) {
        String value = headerMap.get(key);
        if(value == null) {
            throw new IllegalArgumentException("Header not found : " + key);
        }
        return value;
    }

    public Set<Map.Entry<String, String>> gegHeaders() {
        return headerMap.entrySet();
    }

    private HttpHeaders(Map<String, String> headerMap) {
        this.headerMap = new HashMap<>(headerMap);
    }
}
