package codesquad;

import codesquad.handler.ConnectionHandler;
import codesquad.handler.HttpRequestHandler;
import codesquad.handler.HttpResponseHandler;
import codesquad.handler.ResourceHandler;
import codesquad.http.HttpResponseSerializer;
import codesquad.http.header.AcceptHeaderHandler;
import codesquad.server.ServerInitializer;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        HttpRequestHandler httpHandler = new HttpRequestHandler();
        ResourceHandler resourceHandler = new ResourceHandler();
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        AcceptHeaderHandler acceptHeaderHandler = new AcceptHeaderHandler();
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(httpResponseSerializer);
        ConnectionHandler connectionHandler = new ConnectionHandler(httpHandler, resourceHandler, httpResponseHandler, acceptHeaderHandler);

        try {
            serverInitializer.startServer(8080, connectionHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
