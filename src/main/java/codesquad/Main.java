package codesquad;

import codesquad.application.handler.*;
import codesquad.application.processor.HandlerRegistry;
import codesquad.application.processor.HttpRequestDispatcher;
import codesquad.application.database.H2Console;
import codesquad.webserver.authorization.SecurePathManager;
import codesquad.application.database.Database;
import codesquad.application.repository.UserDatabase;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.processor.HttpResponseSerializer;
import codesquad.webserver.middleware.MiddleWareChain;
import codesquad.webserver.middleware.SessionMiddleWare;
import codesquad.application.model.User;
import codesquad.application.model.business.LoginUserLogic;
import codesquad.application.model.business.RegisterUserLogic;
import codesquad.application.model.business.user.GetUserInfoLogic;
import codesquad.application.model.business.user.GetUserListLogic;
import codesquad.application.processor.argumentresolver.ArgumentResolver;
import codesquad.application.processor.argumentresolver.LoginArgumentResolver;
import codesquad.application.processor.argumentresolver.RegisterArgumentResolver;
import codesquad.webserver.processor.HttpRequestParser;
import codesquad.webserver.processor.HttpRequestProcessor;
import codesquad.webserver.processor.HttpResponseWriter;
import codesquad.webserver.server.ServerInitializer;
import codesquad.application.web.user.request.LoginRequest;
import codesquad.application.web.user.request.RegisterRequest;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        CompletableFuture.runAsync(H2Console::main);
        HandlerRegistry handlerRegistry = new HandlerRegistry(new ArrayList<>());
        HttpRequestParser requestParser = new HttpRequestParser();

        // User DB
        Database<User> userDatabase = new UserDatabase();

        // 회원 가입 로직
        RegisterUserLogic registerUserLogic = new RegisterUserLogic(userDatabase);
        ArgumentResolver<RegisterRequest> registerArgumentResolver = new RegisterArgumentResolver();
        RegisterRequestHandler registerUserHandler = new RegisterRequestHandler(registerArgumentResolver);
        handlerRegistry.registerHandler(HttpMethod.POST, "/users/create", registerUserHandler, registerUserLogic);

        // 로그인 로직
        LoginUserLogic loginUserLogic = new LoginUserLogic(userDatabase);
        ArgumentResolver<LoginRequest> loginArgumentResolver = new LoginArgumentResolver();
        LoginRequestHandler loginUserHandler = new LoginRequestHandler(loginArgumentResolver);
        handlerRegistry.registerHandler(HttpMethod.POST, "/users/login", loginUserHandler, loginUserLogic);

        // 로그아웃 API
        LogoutRequestHandler logoutRequestHandlerAdapter = new LogoutRequestHandler();
        handlerRegistry.registerHandler(HttpMethod.POST, "/users/logout", logoutRequestHandlerAdapter, o -> null);

        // User Info API
        GetUserInfoLogic getUserInfoLogic = new GetUserInfoLogic(userDatabase);
        GetUserInfoRequestHandler userInfoHandler = new GetUserInfoRequestHandler();
        handlerRegistry.registerHandler(HttpMethod.GET, "/api/user-info", userInfoHandler, getUserInfoLogic);

        // User List API
        GetUserListLogic getUserListLogic = new GetUserListLogic(userDatabase);
        GetUserListRequestHandler userListHandler = new GetUserListRequestHandler();
        handlerRegistry.registerHandler(HttpMethod.GET, "/api/users", userListHandler, getUserListLogic);


        // 기본 리소스 핸들러
        ResourceHandler<Void, Void> defaultResourceHandler = new ResourceHandler<>();
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        HttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponseSerializer);

        // 디스패처 생성
        HttpRequestDispatcher httpRequestDispatcher = new HttpRequestDispatcher(defaultResourceHandler, handlerRegistry);

        // 미들웨어 생성
        MiddleWareChain middleWareChain = new MiddleWareChain();
        SessionMiddleWare sessionMiddleWare = new SessionMiddleWare();
        middleWareChain.addMiddleWare(sessionMiddleWare);

        // Processor 생성
        HttpRequestProcessor httpRequestProcessor = new HttpRequestProcessor(requestParser, httpRequestDispatcher, httpResponseWriter, middleWareChain);

        SecurePathManager.addSecurePath("/api/user-info", HttpMethod.GET);
        SecurePathManager.addSecurePath("/api/users", HttpMethod.GET);

        try {
            serverInitializer.startServer(8080, httpRequestProcessor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
