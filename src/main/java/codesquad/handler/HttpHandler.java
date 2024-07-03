package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public interface HttpHandler {
    void handle(HttpRequest httpRequest, HttpResponse response) throws Exception;
}
