package codesquad;

import codesquad.handler.HttpRequestDispatcher;
import codesquad.handler.HttpRequestBuilder;
import codesquad.handler.HttpResponseHandler;
import codesquad.handler.ResourceHandler;
import codesquad.http.HttpResponseSerializer;
import codesquad.http.header.AcceptHeaderHandler;
import codesquad.server.ServerInitializer;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        HttpRequestBuilder httpHandler = new HttpRequestBuilder();
        ResourceHandler resourceHandler = new ResourceHandler();
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        AcceptHeaderHandler acceptHeaderHandler = new AcceptHeaderHandler();
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(httpResponseSerializer);
        HttpRequestDispatcher httpRequestDispatcher = new HttpRequestDispatcher(httpHandler, resourceHandler, httpResponseHandler, acceptHeaderHandler);

        try {
            serverInitializer.startServer(8080, httpRequestDispatcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
