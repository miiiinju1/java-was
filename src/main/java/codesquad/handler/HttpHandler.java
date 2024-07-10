package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.processor.Triggerable;

public interface HttpHandler<T, R> {

    void handle(HttpRequest httpRequest, HttpResponse response, Triggerable<T, R> triggerable) throws Exception;
}
