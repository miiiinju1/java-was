package codesquad;

import codesquad.processor.HttpRequestDispatcher;
import codesquad.processor.HttpRequestBuilder;
import codesquad.processor.HttpResponseWriter;
import codesquad.handler.ResourceHandler;
import codesquad.http.HttpResponseSerializer;
import codesquad.server.ServerInitializer;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        HttpRequestBuilder httpHandler = new HttpRequestBuilder();
        ResourceHandler resourceHandler = new ResourceHandler();
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        HttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponseSerializer);
        HttpRequestDispatcher httpRequestDispatcher = new HttpRequestDispatcher(httpHandler, resourceHandler, httpResponseWriter);

        try {
            serverInitializer.startServer(8080, httpRequestDispatcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
