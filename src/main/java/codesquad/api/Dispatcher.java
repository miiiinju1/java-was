package codesquad.api;

import java.io.IOException;

public interface Dispatcher {
    void handleRequest(Request httpRequest, Response httpResponse) throws IOException;
}
