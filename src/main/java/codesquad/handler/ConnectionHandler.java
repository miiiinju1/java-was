package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ConnectionHandler {

    private final HttpRequestHandler httpRequestHandler;
    private final ResourceHandler resourceHandler;
    private final HttpResponseHandler httpResponseHandler;
    private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);

    public ConnectionHandler(HttpRequestHandler httpRequestHandler, ResourceHandler resourceHandler, HttpResponseHandler httpResponseHandler) {
        this.httpRequestHandler = httpRequestHandler;
        this.resourceHandler = resourceHandler;
        this.httpResponseHandler = httpResponseHandler;
    }

    public void handleConnection(final Socket clientSocket) throws IOException {
        final InputStream requestStream = clientSocket.getInputStream();
        final HttpRequest httpRequest = httpRequestHandler.parseRequest(requestStream);

        try {
            // 비즈니스 로직
            final InputStream inputStream = resourceHandler.readFileAsStream(httpRequest.getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            // -----------

            byte[] responseBytes = response.toString().getBytes(StandardCharsets.UTF_8);
            ByteArrayOutputStream body = new ByteArrayOutputStream();
            body.write(responseBytes);

            HttpResponse httpResponse = HttpResponse.builder()
                    .httpVersion(HttpVersion.HTTP_1_1)
                    .httpStatus(HttpStatus.OK)
                    .headers(Map.of("Content-Type", "text/html; charset=UTF-8"))
                    .body(body)
                    .build();

            httpResponseHandler.writeResponse(clientSocket, httpResponse);
        }
        catch (IllegalArgumentException e) {

            log.error("File not found! : {}", httpRequest.getPath());
            httpResponseHandler.writeResponse(clientSocket, HttpResponse.notFoundOf(httpRequest.getPath()));
        }

    }

}
