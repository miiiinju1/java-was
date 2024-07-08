package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.processor.Triggerable;
import codesquad.processor.argumentresolver.ArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ApiRequestHandlerAdapter<T, R> implements HttpHandlerAdapter<T, R> {

    private final ArgumentResolver<T> argumentResolver;
    private HttpStatus status;
    private Map<String, String> headers;

    private static final Logger log = LoggerFactory.getLogger(ApiRequestHandlerAdapter.class);

    public ApiRequestHandlerAdapter(ArgumentResolver<T> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }
    @Override
    public void handle(HttpRequest httpRequest, HttpResponse response, Triggerable<T, R> triggerable) throws Exception {

        T request = argumentResolver.resolve(httpRequest);

        R res = triggerable.run(request);
        log.debug("res = {}", res);
        applyResponseConfig(response, status, headers);

    }
    public void setResponseConfig(HttpStatus status, Map<String, String> headers) {
        this.status = status;
        this.headers = headers;
    }
}
