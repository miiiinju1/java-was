package codesquad.processor;

import codesquad.handler.HttpHandlerAdapter;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestDispatcher {

    private final HttpHandlerAdapter<?, ?> defaultHandler;
    private final HandlerRegistry handlerRegistry;
    private static final Logger log = LoggerFactory.getLogger(HttpRequestDispatcher.class);

    public HttpRequestDispatcher(
                         HttpHandlerAdapter<?,?> defaultHandler,
                         HandlerRegistry handlerRegistry
    ) {
        this.defaultHandler = defaultHandler;
        this.handlerRegistry = handlerRegistry;
    }


    public void handleConnection(final HttpRequest httpRequest, final HttpResponse httpResponse) throws Exception {
        // 핸들러를 찾아서 실행 (현재는 리소스 핸들러만 존재)
        HandlerMapping<?, ?> mapping = handlerRegistry.getHandler(httpRequest.getMethod(), httpRequest.getPath());

        if(mapping != null) {
            handleRequestWithMapping(httpRequest, httpResponse, mapping);
        }
        // 만약 API 핸들러가 없다면 디폴트 핸들러 (리소스 핸들러) 실행
        else {
            handleRequestWithDefaultHandler(httpRequest, httpResponse);
        }
    }

    private <T, R> void handleRequestWithMapping(HttpRequest httpRequest, HttpResponse httpResponse, HandlerMapping<T, R> mapping) throws Exception {
        HttpHandlerAdapter<T, R> handler = mapping.getHandler();
        Triggerable<T, R> triggerable = mapping.getTrigger();
        handler.handle(httpRequest, httpResponse, triggerable);
    }

    private void handleRequestWithDefaultHandler(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        defaultHandler.handle(httpRequest, httpResponse, null);
    }

}
