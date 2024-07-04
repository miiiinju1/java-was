package codesquad.processor;

import codesquad.handler.HttpHandlerAdapter;
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
    private final HttpHandlerAdapter<?> defaultHandler;
    private final HttpResponseWriter httpResponseWriter;
    private final HandlerRegistry handlerRegistry;
    private static final Logger log = LoggerFactory.getLogger(HttpRequestDispatcher.class);

    public HttpRequestDispatcher(HttpRequestBuilder httpRequestBuilder,
                                 HttpHandlerAdapter<?> defaultHandler,
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
            HandlerMapping<?> mapping = handlerRegistry.getHandler(httpRequest.getMethod(), httpRequest.getPath());

            if(mapping != null) {
                handleRequestWithMapping(httpRequest, httpResponse, mapping);
            }
            // 만약 API 핸들러가 없다면 디폴트 핸들러 (리소스 핸들러) 실행
            else {
                handleRequestWithDefaultHandler(httpRequest, httpResponse);
            }
        } catch (Exception e) {
            log.error("Failed to handle request", e);
            httpResponseWriter.writeResponse(clientSocket, HttpResponse.notFoundOf(httpRequest.getPath().getBasePath()));
            return;
        }

        // 결과 전송
        httpResponseWriter.writeResponse(clientSocket, httpResponse);
    }

    private <R> void handleRequestWithMapping(HttpRequest httpRequest, HttpResponse httpResponse, HandlerMapping<R> mapping) throws Exception {
        HttpHandlerAdapter<R> handler = mapping.getHandler();
        Triggerable<R> triggerable = mapping.getTrigger();
        handler.handle(httpRequest, httpResponse, triggerable);
    }

    private void handleRequestWithDefaultHandler(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        defaultHandler.handle(httpRequest, httpResponse, null);
    }

}
