package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.processor.Triggerable;

public interface HttpHandlerAdapter<R> {
    void handle(HttpRequest httpRequest, HttpResponse response, Triggerable<R> triggerable) throws Exception;
}
