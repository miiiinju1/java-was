package codesquad;

import codesquad.authorization.SecurePathManager;
import codesquad.database.Database;
import codesquad.handler.*;
import codesquad.http.HttpMethod;
import codesquad.http.HttpResponseSerializer;
import codesquad.middleware.MiddleWareChain;
import codesquad.middleware.SessionMiddleWare;
import codesquad.model.User;
import codesquad.model.business.LoginUserLogic;
import codesquad.model.business.RegisterUserLogic;
import codesquad.model.business.user.GetUserInfoLogic;
import codesquad.model.business.user.GetUserListLogic;
import codesquad.processor.*;
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
        HttpRequestParser requestParser = new HttpRequestParser();

        // User DB
        Database<User> userDatabase = new Database<>();

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
        ResourceHandlerAdapter<Void, Void> defaultResourceHandler = new ResourceHandlerAdapter<>();
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
