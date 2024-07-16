package codesquad;

import codesquad.application.database.DatabaseConfig;
import codesquad.application.database.H2Console;
import codesquad.application.database.dao.PostDao;
import codesquad.application.database.dao.PostDaoImpl;
import codesquad.application.database.dao.UserDao;
import codesquad.application.database.dao.UserDaoImpl;
import codesquad.application.handler.*;
import codesquad.application.model.business.LoginUserLogic;
import codesquad.application.model.business.RegisterUserLogic;
import codesquad.application.model.business.user.GetUserInfoLogic;
import codesquad.application.model.business.user.GetUserListLogic;
import codesquad.application.domain.post.argumentresolver.PostCreateArgumentResolver;
import codesquad.application.domain.post.business.PostCreateLogic;
import codesquad.application.domain.post.handler.PostCreateRequestHandler;
import codesquad.application.domain.post.request.PostCreateRequest;
import codesquad.application.processor.HandlerRegistry;
import codesquad.application.processor.HttpRequestDispatcher;
import codesquad.application.processor.argumentresolver.ArgumentResolver;
import codesquad.application.processor.argumentresolver.LoginArgumentResolver;
import codesquad.application.processor.argumentresolver.RegisterArgumentResolver;
import codesquad.application.web.user.request.LoginRequest;
import codesquad.application.web.user.request.RegisterRequest;
import codesquad.webserver.authorization.SecurePathManager;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.middleware.MiddleWareChain;
import codesquad.webserver.middleware.SessionMiddleWare;
import codesquad.webserver.processor.HttpRequestParser;
import codesquad.webserver.processor.HttpRequestProcessor;
import codesquad.webserver.processor.HttpResponseSerializer;
import codesquad.webserver.processor.HttpResponseWriter;
import codesquad.webserver.server.ServerInitializer;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Main {


    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        DatabaseConfig databaseConfig = new DatabaseConfig("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");

        CompletableFuture.runAsync(() -> H2Console.main(databaseConfig));
        HandlerRegistry handlerRegistry = new HandlerRegistry(new ArrayList<>());
        HttpRequestParser requestParser = new HttpRequestParser();

        // User DB
        UserDao userDao = new UserDaoImpl(databaseConfig);

        // Post DB
        PostDao postDao = new PostDaoImpl(databaseConfig);

        // 회원 가입 로직
        RegisterUserLogic registerUserLogic = new RegisterUserLogic(userDao);
        ArgumentResolver<RegisterRequest> registerArgumentResolver = new RegisterArgumentResolver();
        RegisterRequestHandler registerUserHandler = new RegisterRequestHandler(registerArgumentResolver);
        handlerRegistry.registerHandler(HttpMethod.POST, "/users/create", registerUserHandler, registerUserLogic);

        // 로그인 로직
        LoginUserLogic loginUserLogic = new LoginUserLogic(userDao);
        ArgumentResolver<LoginRequest> loginArgumentResolver = new LoginArgumentResolver();
        LoginRequestHandler loginUserHandler = new LoginRequestHandler(loginArgumentResolver);
        handlerRegistry.registerHandler(HttpMethod.POST, "/users/login", loginUserHandler, loginUserLogic);

        // 로그아웃 API
        LogoutRequestHandler logoutRequestHandlerAdapter = new LogoutRequestHandler();
        handlerRegistry.registerHandler(HttpMethod.POST, "/users/logout", logoutRequestHandlerAdapter, o -> null);

        // User Info API
        GetUserInfoLogic getUserInfoLogic = new GetUserInfoLogic(userDao);
        GetUserInfoRequestHandler userInfoHandler = new GetUserInfoRequestHandler();
        handlerRegistry.registerHandler(HttpMethod.GET, "/api/user-info", userInfoHandler, getUserInfoLogic);

        // User List API
        GetUserListLogic getUserListLogic = new GetUserListLogic(userDao);
        GetUserListRequestHandler userListHandler = new GetUserListRequestHandler();
        handlerRegistry.registerHandler(HttpMethod.GET, "/api/users", userListHandler, getUserListLogic);


        // 기본 리소스 핸들러
        StaticResourceHandler<Void, Void> defaultResourceHandler = new StaticResourceHandler<>();
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        HttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponseSerializer);

        // Post
        registerPostApi(handlerRegistry, userDao, postDao);

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

    private static void registerPostApi(
            HandlerRegistry handlerRegistry,
            UserDao userDao,
            PostDao postDao
    ) {
        PostCreateLogic postCreateLogic = new PostCreateLogic(userDao, postDao);
        ArgumentResolver<PostCreateRequest> postCreateArgumentResolver = new PostCreateArgumentResolver();
        PostCreateRequestHandler postCreateRequestHandler = new PostCreateRequestHandler(postCreateArgumentResolver);
        handlerRegistry.registerHandler(HttpMethod.POST, "/api/posts", postCreateRequestHandler, postCreateLogic);

//        PostListLogic postListLogic = new PostListLogic(postDao);
//        PostListRequestHandler postListRequestHandler = new PostListRequestHandler();
//        handlerRegistry.registerHandler(HttpMethod.GET, "/api/posts", postListRequestHandler, postListLogic);
//
//        PostDeleteLogic postDeleteLogic = new PostDeleteLogic(postDao);
//        ArgumentResolver<PostDeleteRequest> postDeleteArgumentResolver = new PostDeleteArgumentResolver();
//        PostDeleteRequestHandler postDeleteRequestHandler = new PostDeleteRequestHandler(postDeleteArgumentResolver);
//        handlerRegistry.registerHandler(HttpMethod.DELETE, "/api/posts", postDeleteRequestHandler, postDeleteLogic);
    }
}
