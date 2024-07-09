package codesquad.middleware;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public interface MiddleWare {

    void applyMiddleWare(HttpRequest request, HttpResponse response);
}
