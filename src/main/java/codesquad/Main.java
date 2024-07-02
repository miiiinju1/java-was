package codesquad;

import codesquad.handler.ConnectionHandler;
import codesquad.handler.ResourceHandler;
import codesquad.handler.HttpRequestHandler;
import codesquad.server.ServerInitializer;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        HttpRequestHandler httpHandler = new HttpRequestHandler();
        ResourceHandler resourceHandler = new ResourceHandler();
        ConnectionHandler connectionHandler = new ConnectionHandler(httpHandler, resourceHandler);

        try {
            serverInitializer.startServer(8080, connectionHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
