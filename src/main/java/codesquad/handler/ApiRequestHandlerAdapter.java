package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.processor.Triggerable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class ApiRequestHandlerAdapter<T, R> implements HttpHandlerAdapter<T, R> {

    private HttpStatus status;
    private Map<String, String> headers;
    private static final Logger log = LoggerFactory.getLogger(ApiRequestHandlerAdapter.class);

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse response, Triggerable<T, R> triggerable) throws Exception {

        T request = resolveArgument(httpRequest);
        R res = triggerable.run(request);
        log.debug("res = {}", res);
        applyResponseConfig(response, status, headers);
        afterHandle(request, res, httpRequest, response);

    }

    public abstract void afterHandle(T request, R response, HttpRequest httpRequest, HttpResponse httpResponse);

    public abstract T resolveArgument(HttpRequest httpRequest);

    public void setResponseConfig(HttpStatus status, Map<String, String> headers) {
        this.status = status;
        this.headers = headers;
    }
}
