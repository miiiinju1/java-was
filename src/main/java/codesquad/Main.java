package codesquad;

import codesquad.database.Database;
import codesquad.handler.LoginRequestHandlerAdapter;
import codesquad.handler.LogoutRequestHandlerAdapter;
import codesquad.handler.RegisterRequestHandlerAdapter;
import codesquad.handler.ResourceHandlerAdapter;
import codesquad.http.HttpMethod;
import codesquad.http.HttpResponseSerializer;
import codesquad.model.User;
import codesquad.model.business.LoginUserLogic;
import codesquad.model.business.RegisterUserLogic;
import codesquad.processor.HandlerRegistry;
import codesquad.processor.HttpRequestBuilder;
import codesquad.processor.HttpRequestDispatcher;
import codesquad.processor.HttpResponseWriter;
import codesquad.processor.argumentresolver.ArgumentResolver;
import codesquad.processor.argumentresolver.LoginArgumentResolver;
import codesquad.processor.argumentresolver.RegisterArgumentResolver;
import codesquad.server.ServerInitializer;
import codesquad.web.user.LoginRequest;
import codesquad.web.user.RegisterRequest;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();

        HandlerRegistry handlerRegistry = new HandlerRegistry(new ArrayList<>());
        HttpRequestBuilder httpHandler = new HttpRequestBuilder();

        // User DB
        Database<User> userDatabase = new Database<>();

        // 회원 가입 로직
        RegisterUserLogic registerUserLogic = new RegisterUserLogic(userDatabase);
        ArgumentResolver<RegisterRequest> registerArgumentResolver = new RegisterArgumentResolver();
        RegisterRequestHandlerAdapter registerUserHandler = new RegisterRequestHandlerAdapter(registerArgumentResolver);
        handlerRegistry.registerHandler(HttpMethod.POST, "/users/create", registerUserHandler, registerUserLogic);

        // 로그인 로직
        // TODO 로그인 실패 시 예외 전파하여 failed index.html 찾게 변경하기
        // 쿠키 발급 구현
        // ApiRequestHandlerAdapter를 공통적으로 구현하고, 추가적으로 setStatus나 redirectUrl은 추가 메서드를 통해 설정하도록 변경하기
        LoginUserLogic loginUserLogic = new LoginUserLogic(userDatabase);
        ArgumentResolver<LoginRequest> loginArgumentResolver = new LoginArgumentResolver();
        LoginRequestHandlerAdapter loginUserHandler = new LoginRequestHandlerAdapter(loginArgumentResolver);
        handlerRegistry.registerHandler(HttpMethod.POST, "/users/login", loginUserHandler, loginUserLogic);

        // 로그아웃 API
        LogoutRequestHandlerAdapter logoutRequestHandlerAdapter = new LogoutRequestHandlerAdapter();
        handlerRegistry.registerHandler(HttpMethod.POST, "/users/logout", logoutRequestHandlerAdapter, o -> null);

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
