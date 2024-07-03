package codesquad.handler;

import codesquad.http.*;
import codesquad.http.header.AcceptHeaderHandler;
import codesquad.http.header.HeaderConstants;
import codesquad.http.header.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class ConnectionHandler {

    private final HttpRequestBuilder httpRequestBuilder;
    private final ResourceHandler resourceHandler;
    private final HttpResponseHandler httpResponseHandler;
    private final AcceptHeaderHandler acceptHeaderHandler;
    private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);

    public ConnectionHandler(HttpRequestBuilder httpRequestBuilder, ResourceHandler resourceHandler, HttpResponseHandler httpResponseHandler, AcceptHeaderHandler acceptHeaderHandler) {
        this.httpRequestBuilder = httpRequestBuilder;
        this.resourceHandler = resourceHandler;
        this.httpResponseHandler = httpResponseHandler;
        this.acceptHeaderHandler = acceptHeaderHandler;
    }

    public void handleConnection(final Socket clientSocket) throws IOException {
        final InputStream requestStream = clientSocket.getInputStream();
        final HttpRequest httpRequest = httpRequestBuilder.parseRequest(requestStream);

        byte[] responseBody = null;
        try {
            // 비즈니스 로직
            responseBody = handleBusinessLogic(httpRequest);
        }
        catch (IllegalArgumentException e) {
            log.error("File not found! : {}", httpRequest.getPath());
            httpResponseHandler.writeResponse(clientSocket, HttpResponse.notFoundOf(httpRequest.getPath().getValue()));
        }
        // -----------

        HttpResponse httpResponse = buildHttpResponse(httpRequest, responseBody);
        httpResponseHandler.writeResponse(clientSocket, httpResponse);
    }

    private byte[] handleBusinessLogic(HttpRequest httpRequest) throws IOException {
        try (final InputStream inputStream = resourceHandler.readFileAsStream(httpRequest.getPath().getValue());
             ByteArrayOutputStream body = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                body.write(buffer, 0, bytesRead);
            }
            return body.toByteArray();
        } catch (IllegalArgumentException e) {
            log.error("File not found! : {}", httpRequest.getPath());
            throw new FileNotFoundException("File not found: " + httpRequest.getPath().getValue());
        }
    }

    private HttpResponse buildHttpResponse(HttpRequest httpRequest, byte[] responseBody) throws IOException {
        HttpHeaders clientHeaders = httpRequest.getHeaders();

        Mime mime = null;
        if(!clientHeaders.containsHeader(HeaderConstants.ACCEPT)) {
            mime = Mime.valueOf(httpRequest.getPath().getValue());
        }
        else {
            mime = acceptHeaderHandler.getMimeFromAcceptHeader(clientHeaders.getHeader(HeaderConstants.ACCEPT));
        }

        ByteArrayOutputStream body = new ByteArrayOutputStream();
        body.write(responseBody);

        Map<String, String> headers;
        if (httpRequest.getPath().getValue().endsWith("favicon.ico")) {
            headers = Map.of(HeaderConstants.CONTENT_TYPE, Mime.IMAGE_ICO.getType());
        }
        else if (httpRequest.getPath().getValue().endsWith("svg")) {
            headers = Map.of(HeaderConstants.CONTENT_TYPE, Mime.IMAGE_SVG.getType());
        }
        else {
            headers = Map.of(HeaderConstants.CONTENT_TYPE, mime.getType());
        }

        return HttpResponse.builder()
                .httpVersion(HttpVersion.HTTP_1_1)
                .httpStatus(HttpStatus.OK)
                .headers(headers)
                .body(body)
                .build();
    }

}
