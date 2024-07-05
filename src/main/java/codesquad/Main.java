package codesquad;

import codesquad.handler.ApiRequestHandlerAdapter;
import codesquad.handler.ResourceHandlerAdapter;
import codesquad.http.HttpMethod;
import codesquad.http.HttpResponseSerializer;
import codesquad.model.business.RegisterUserLogic;
import codesquad.processor.*;
import codesquad.server.ServerInitializer;
import codesquad.web.user.RegisterRequest;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();

        HandlerRegistry handlerRegistry = new HandlerRegistry(new ArrayList<>());
        HttpRequestBuilder httpHandler = new HttpRequestBuilder();

        // 회원 가입 로직
        RegisterUserLogic registerUserLogic = new RegisterUserLogic();
        ArgumentResolver<RegisterRequest> registerArgumentResolver = new RegisterArgumentResolver();
        ApiRequestHandlerAdapter<RegisterRequest, Void> registerUserHandler = new ApiRequestHandlerAdapter<>(registerArgumentResolver);
        handlerRegistry.registerHandler(HttpMethod.GET, "/create", registerUserHandler, registerUserLogic);

        // 기본 리소스 핸들러
        ResourceHandlerAdapter<Void, Void> defaultResourceHandler = new ResourceHandlerAdapter<>();
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        HttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponseSerializer);
        HttpRequestDispatcher httpRequestDispatcher = new HttpRequestDispatcher(httpHandler, defaultResourceHandler, httpResponseWriter, handlerRegistry);

        try {
            serverInitializer.startServer(8080, httpRequestDispatcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
