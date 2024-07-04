package codesquad;

import codesquad.handler.ApiRequestHandler;
import codesquad.http.HttpMethod;
import codesquad.processor.HandlerRegistry;
import codesquad.processor.HttpRequestDispatcher;
import codesquad.processor.HttpRequestBuilder;
import codesquad.processor.HttpResponseWriter;
import codesquad.handler.ResourceHandler;
import codesquad.http.HttpResponseSerializer;
import codesquad.server.ServerInitializer;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();

        HandlerRegistry handlerRegistry = new HandlerRegistry(new ArrayList<>());
        handlerRegistry.registerHandler(HttpMethod.GET, "/create", new ApiRequestHandler());

        HttpRequestBuilder httpHandler = new HttpRequestBuilder();
        ResourceHandler resourceHandler = new ResourceHandler();
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        HttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponseSerializer);
        HttpRequestDispatcher httpRequestDispatcher = new HttpRequestDispatcher(httpHandler, resourceHandler, httpResponseWriter, handlerRegistry);

        try {
            serverInitializer.startServer(8080, httpRequestDispatcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
