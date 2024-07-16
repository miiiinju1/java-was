package codesquad.application.helper;

import java.util.HashMap;
import java.util.Map;

public class JsonDeserializer {

    public static Map<String, String> simpleConvertJsonToMap(final String string) {
        Map<String, String> jsonMap = new HashMap<>();

        if (string == null) {
            throw new NullPointerException("Input string is null");
        }

        // Remove the curly braces
        String jsonString = string.trim();
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1);
        }

        // Split the key-value pairs
        String[] keyValuePairs = jsonString.split("(?<!\\\\),"); // Handles commas not preceded by a backslash

        for (String pair : keyValuePairs) {
            // Split the key and value
            String[] entry = pair.split("(?<!\\\\):"); // Handles colons not preceded by a backslash
            if (entry.length == 2) {
                String key = entry[0].trim().replaceAll("^\"|\"$", "").replace("\\,", ",").replace("\\:", ":"); // Remove leading and trailing quotes, handle escaped characters
                String value = entry[1].trim().replaceAll("^\"|\"$", "").replace("\\,", ",").replace("\\:", ":"); // Remove leading and trailing quotes, handle escaped characters
                jsonMap.put(key, value);
            } else {
                // Handle invalid format
                throw new IllegalArgumentException("Invalid JSON format");
            }
        }

        return jsonMap;
    }

    private JsonDeserializer() {
    }
}
