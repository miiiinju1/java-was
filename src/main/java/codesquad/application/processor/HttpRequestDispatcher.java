package codesquad.application.processor;

import codesquad.api.Dispatcher;
import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.application.handler.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpRequestDispatcher implements Dispatcher {

    private final HttpHandler<?, ?> defaultHandler;
    private final HandlerRegistry handlerRegistry;
    private static final Logger log = LoggerFactory.getLogger(HttpRequestDispatcher.class);

    public HttpRequestDispatcher(
                         HttpHandler<?,?> defaultHandler,
                         HandlerRegistry handlerRegistry
    ) {
        this.defaultHandler = defaultHandler;
        this.handlerRegistry = handlerRegistry;
    }

    @Override
    public void handleRequest(final Request httpRequest, final Response httpResponse) throws IOException {
        // 핸들러를 찾아서 실행 (현재는 리소스 핸들러만 존재)
        HandlerMapping<?, ?> mapping = handlerRegistry.getHandler(httpRequest.getMethod(), httpRequest.getPath());

        if(mapping != null) {
            handleRequestWithMapping(httpRequest, httpResponse, mapping);
            return ;
        }
        // 만약 API 핸들러가 없다면 디폴트 핸들러 (리소스 핸들러) 실행
        handleRequestWithDefaultHandler(httpRequest, httpResponse);
    }

    private <T, R> void handleRequestWithMapping(Request httpRequest, Response httpResponse, HandlerMapping<T, R> mapping) throws IOException {
        HttpHandler<T, R> handler = mapping.getHandler();
        Triggerable<T, R> triggerable = mapping.getTrigger();
        handler.handle(httpRequest, httpResponse, triggerable);
    }

    private void handleRequestWithDefaultHandler(Request httpRequest, Response httpResponse) throws IOException {
        defaultHandler.handle(httpRequest, httpResponse, null);
    }

}
