package codesquad;

import codesquad.handler.ConnectionHandler;
import codesquad.handler.ResourceHandler;
import codesquad.handler.HttpHandler;
import codesquad.server.ServerInitializer;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        HttpHandler httpHandler = new HttpHandler();
        ResourceHandler resourceHandler = new ResourceHandler();
        ConnectionHandler connectionHandler = new ConnectionHandler(httpHandler, resourceHandler);

        try {
            serverInitializer.startServer(8080, connectionHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
