package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.processor.Triggerable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ApiRequestHandlerAdapter<T, R> implements HttpHandlerAdapter<T, R> {

    private static final Logger log = LoggerFactory.getLogger(ApiRequestHandlerAdapter.class);

    @Override
    public final void handle(HttpRequest httpRequest, HttpResponse response, Triggerable<T, R> triggerable) throws Exception {
        T request = resolveArgument(httpRequest);
        try {
            R res = triggerable.run(request);
            log.debug("res = {}", res);
            afterHandle(request, res, httpRequest, response);

            if(res == null) {
                return;
            }
            String serializedResponse = serializeResponse(res);

            response.getBody().write(serializedResponse.getBytes());
        }
        catch (RuntimeException e) {
            log.error("Exception : {}", e.getMessage());
            applyExceptionHandler(e, response);
        }
    }

    private static final String DEFAULT_EMPTY_RESPONSE = "";

    public String serializeResponse(R response) {
        return DEFAULT_EMPTY_RESPONSE;
    }

    public abstract void afterHandle(T request, R response, HttpRequest httpRequest, HttpResponse httpResponse);

    public abstract T resolveArgument(HttpRequest httpRequest);

    public abstract void applyExceptionHandler(RuntimeException e, HttpResponse response);

}
