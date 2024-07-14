package codesquad.application.handler;

import codesquad.application.processor.Triggerable;
import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.webserver.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class ApiRequestHandler<T, R> implements HttpHandler<T, R> {

    private static final Logger log = LoggerFactory.getLogger(ApiRequestHandler.class);

    @Override
    public final void handle(Request httpRequest, Response httpResponse, Triggerable<T, R> triggerable) throws IOException {
        T request = resolveArgument(httpRequest);
        httpResponse.setStatus(HttpStatus.OK);
        try {
            R res = triggerable.run(request);
            log.debug("res = {}", res);
            afterHandle(request, res, httpRequest, httpResponse);

            if(res == null) {
                return;
            }
            String serializedResponse = serializeResponse(res);
            httpResponse.getBody().write(serializedResponse.getBytes());
        }
        catch (RuntimeException e) {
            log.error("Exception : {}", e.getMessage());
            applyExceptionHandler(e, httpResponse);
        }
    }

    private static final String DEFAULT_EMPTY_RESPONSE = "";

    public String serializeResponse(R response) {
        return DEFAULT_EMPTY_RESPONSE;
    }

    public abstract void afterHandle(T request, R response, Request httpRequest, Response httpResponse);

    public abstract T resolveArgument(Request httpRequest);

    public abstract void applyExceptionHandler(RuntimeException e, Response response) throws IOException;

}
