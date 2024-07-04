package codesquad.processor;

import codesquad.handler.ApiRequestHandler;
import codesquad.handler.ResourceHandler;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HttpRequestDispatcher {

    private final HttpRequestBuilder httpRequestBuilder;
    private final ResourceHandler resourceHandler;
    private final HttpResponseWriter httpResponseWriter;
    private final ApiRequestHandler apiRequestHandler;
    private static final Logger log = LoggerFactory.getLogger(HttpRequestDispatcher.class);

    public HttpRequestDispatcher(HttpRequestBuilder httpRequestBuilder,
                                 ResourceHandler resourceHandler,
                                 HttpResponseWriter httpResponseWriter,
                                    ApiRequestHandler apiRequestHandler
    ) {
        this.httpRequestBuilder = httpRequestBuilder;
        this.resourceHandler = resourceHandler;
        this.httpResponseWriter = httpResponseWriter;
        this.apiRequestHandler = apiRequestHandler;
    }

    public void handleConnection(final Socket clientSocket) throws IOException {
        final InputStream requestStream = clientSocket.getInputStream();
        final HttpRequest httpRequest = httpRequestBuilder.parseRequest(requestStream);
        final HttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_1_1);

        // 핸들러를 찾아서 실행 (현재는 리소스 핸들러만 존재)
        try {
            // TODO 임시로 create로 구분하게 만듦
            if(httpRequest.getPath().getValue().startsWith("/create")) {
                apiRequestHandler.handle(httpRequest, httpResponse);
            } else {
                resourceHandler.handle(httpRequest, httpResponse);
            }
        } catch (Exception e) {
            log.error("Failed to handle request", e);
            httpResponseWriter.writeResponse(clientSocket, HttpResponse.notFoundOf(httpRequest.getPath().getValue()));
            return;
        }

        httpResponseWriter.writeResponse(clientSocket, httpResponse);
    }
}
