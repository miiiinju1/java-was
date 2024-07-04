package codesquad.processor;

import codesquad.handler.HttpHandler;
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
    private final HttpHandler defaultHandler;
    private final HttpResponseWriter httpResponseWriter;
    private final HandlerRegistry handlerRegistry;
    private static final Logger log = LoggerFactory.getLogger(HttpRequestDispatcher.class);

    public HttpRequestDispatcher(HttpRequestBuilder httpRequestBuilder,
                                 ResourceHandler defaultHandler,
                                 HttpResponseWriter httpResponseWriter,
                                 HandlerRegistry handlerRegistry
    ) {
        this.httpRequestBuilder = httpRequestBuilder;
        this.defaultHandler = defaultHandler;
        this.httpResponseWriter = httpResponseWriter;
        this.handlerRegistry = handlerRegistry;
    }

    public void handleConnection(final Socket clientSocket) throws IOException {
        final InputStream requestStream = clientSocket.getInputStream();
        final HttpRequest httpRequest = httpRequestBuilder.parseRequest(requestStream);
        final HttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_1_1);

        // 핸들러를 찾아서 실행 (현재는 리소스 핸들러만 존재)
        try {
            HandlerMapping mapping = handlerRegistry.getHandler(httpRequest.getMethod(), httpRequest.getPath());
            if(mapping != null) {
                mapping.getHandler().handle(httpRequest, httpResponse);
            }
            // 만약 API 핸들러가 없다면 디폴트 핸들러 (리소스 핸들러) 실행
            else {
                defaultHandler.handle(httpRequest, httpResponse);
            }
        } catch (Exception e) {
            log.error("Failed to handle request", e);
            httpResponseWriter.writeResponse(clientSocket, HttpResponse.notFoundOf(httpRequest.getPath().getBasePath()));
            return;
        }

        httpResponseWriter.writeResponse(clientSocket, httpResponse);
    }
}
