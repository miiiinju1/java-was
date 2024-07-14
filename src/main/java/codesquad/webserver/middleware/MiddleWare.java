package codesquad.webserver.middleware;

import codesquad.api.Request;
import codesquad.api.Response;

public interface MiddleWare {

    void applyMiddleWare(Request httpRequest, Response httpResponse);
}
