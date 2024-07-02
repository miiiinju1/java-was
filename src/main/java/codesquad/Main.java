package codesquad;

import codesquad.handler.ConnectionHandler;
import codesquad.handler.HttpRequestHandler;
import codesquad.handler.HttpResponseHandler;
import codesquad.handler.ResourceHandler;
import codesquad.server.ServerInitializer;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        HttpRequestHandler httpHandler = new HttpRequestHandler();
        ResourceHandler resourceHandler = new ResourceHandler();
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler();
        ConnectionHandler connectionHandler = new ConnectionHandler(httpHandler, resourceHandler, httpResponseHandler);

        try {
            serverInitializer.startServer(8080, connectionHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
