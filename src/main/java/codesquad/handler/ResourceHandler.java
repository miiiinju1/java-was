package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.Mime;
import codesquad.http.header.HeaderConstants;
import codesquad.http.header.HttpHeaders;
import codesquad.mapper.MimeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceHandler implements HttpHandler {

    private static final String STATIC_PATH = "static";
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);

    private static final MimeMapper mimeMapper = new MimeMapper();


    public InputStream readFileAsStream(String filePath) {
        ClassLoader classLoader = ResourceHandler.class.getClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(STATIC_PATH + filePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! : static/" + filePath);
        }

        return inputStream;
    }

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
            log.error("File not found! : {}", request.getPath());
            throw new FileNotFoundException("File not found: " + request.getPath().getValue());
        }

        Mime mime = null;
        HttpHeaders clientHeaders = request.getHeaders();
        if(!clientHeaders.containsHeader(HeaderConstants.ACCEPT)) {
            mime = Mime.valueOf(request.getPath().getValue());
        }
        else {
            mime = mimeMapper.getMimeFromAcceptHeader(clientHeaders.getHeader(HeaderConstants.ACCEPT));
        }

        if (request.getPath().getValue().endsWith("favicon.ico")) {
            mime = Mime.IMAGE_ICO;
        }
        else if (request.getPath().getValue().endsWith("svg")) {
            mime = Mime.IMAGE_SVG;
        }

        response.getHttpHeaders()
                .addHeader(HeaderConstants.CONTENT_TYPE, mime.getType());

    }
}
