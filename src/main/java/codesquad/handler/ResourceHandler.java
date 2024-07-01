package codesquad.handler;

import java.io.InputStream;

public class ResourceHandler {

    private static final String STATIC_PATH = "static/";

    public InputStream readFileAsStream(String filePath) {
        ClassLoader classLoader = ResourceHandler.class.getClassLoader();

        classLoader.getResource(STATIC_PATH + filePath);

        InputStream inputStream = classLoader.getResourceAsStream(STATIC_PATH + filePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! : static/" + filePath);
        }

        return inputStream;
    }
}
