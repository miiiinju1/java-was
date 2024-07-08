package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.processor.Triggerable;

import java.util.Map;

public interface HttpHandlerAdapter<T, R> {
    void handle(HttpRequest httpRequest, HttpResponse response, Triggerable<T, R> triggerable) throws Exception;

    default void applyResponseConfig(HttpResponse response, HttpStatus status, Map<String, String> headers) {
        if (status != null) {
            response.setStatus(status);
        }

        if (headers != null) {
            headers.forEach(response::setHeader);
        }
    }
}
