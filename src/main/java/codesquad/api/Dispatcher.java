package codesquad.api;

public interface Dispatcher {
    void handleRequest(Request httpRequest, Response httpResponse) throws Exception;
}
