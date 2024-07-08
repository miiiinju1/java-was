package codesquad.helper;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestBodyParseHelper {

    public static Map<String, String> bodyParameters(final String bodyString) {
        return Arrays.stream(bodyString.split("&"))
                .map(s -> {
                    String[] split = s.split("=");
                    String key = split[0];
                    String value = split.length > 1 ? split[1] : "";

                    return Map.entry(key, value);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private RequestBodyParseHelper() {
    }
}
