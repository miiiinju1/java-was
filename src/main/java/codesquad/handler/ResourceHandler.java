package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.Mime;
import codesquad.http.header.HeaderConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class ResourceHandler implements HttpHandler {

    private static final String STATIC_PATH = "static";
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws Exception {
        final String filePath = request.getPath().getValue();

        ClassLoader classLoader = ResourceHandler.class.getClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(STATIC_PATH + filePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! : static/" + filePath);
        }

        try (inputStream) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getBody().write(buffer, 0, bytesRead);
            }
        } catch (IllegalArgumentException e) {
            log.error("Failed to read file", e);
            throw e;
        }

        Mime mime = Mime.ofFilePath(filePath);
        response.getHttpHeaders()
                .addHeader(HeaderConstants.CONTENT_TYPE, mime.getType());

    }
}
