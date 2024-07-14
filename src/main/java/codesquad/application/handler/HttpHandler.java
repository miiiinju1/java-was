package codesquad.application.handler;

import codesquad.application.processor.Triggerable;
import codesquad.api.Request;
import codesquad.api.Response;

public interface HttpHandler<T, R> {

    void handle(Request httpRequest, Response httpResponse, Triggerable<T, R> triggerable) throws Exception;
}
