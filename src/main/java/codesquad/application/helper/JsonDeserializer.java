package codesquad.application.helper;

import java.util.HashMap;
import java.util.Map;

public class JsonDeserializer {

    public static Map<String, String> simpleConvertJsonToMap(final String string) {
        Map<String, String> jsonMap = new HashMap<>();

        // Remove the curly braces
        String jsonString = string.trim();
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1);
        }

        // Split the key-value pairs
        String[] keyValuePairs = jsonString.split(",");

        for (String pair : keyValuePairs) {
            // Split the key and value
            String[] entry = pair.split(":");
            if (entry.length == 2) {
                String key = entry[0].trim().replaceAll("^\"|\"$", ""); // Remove leading and trailing quotes
                String value = entry[1].trim().replaceAll("^\"|\"$", ""); // Remove leading and trailing quotes
                jsonMap.put(key, value);
            }
        }

        return jsonMap;
    }
}
