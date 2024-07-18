package codesquad;

import codesquad.application.database.DatabaseConfig;
import codesquad.application.database.dao.*;
import codesquad.application.domain.comment.argumentresolver.CreateCommentArgumentResolver;
import codesquad.application.domain.comment.business.CreateCommentLogic;
import codesquad.application.domain.comment.handler.CreateCommentRequestHandler;
import codesquad.application.domain.comment.request.CreateCommentRequest;
import codesquad.application.domain.images.handler.ImageResourceHandler;
import codesquad.application.domain.post.argumentresolver.PostCreateArgumentResolver;
import codesquad.application.domain.post.business.GetPostListLogic;
import codesquad.application.domain.post.business.PostCreateLogic;
import codesquad.application.domain.post.handler.GetPostListRequestHandler;
import codesquad.application.domain.post.handler.PostCreateRequestHandler;
import codesquad.application.domain.post.request.PostCreateRequest;
import codesquad.application.domain.user.argumentresolver.LoginArgumentResolver;
import codesquad.application.domain.user.argumentresolver.RegisterArgumentResolver;
import codesquad.application.domain.user.business.GetUserInfoLogic;
import codesquad.application.domain.user.business.GetUserListLogic;
import codesquad.application.domain.user.business.LoginUserLogic;
import codesquad.application.domain.user.business.RegisterUserLogic;
import codesquad.application.domain.user.handler.*;
import codesquad.application.domain.user.request.LoginRequest;
import codesquad.application.domain.user.request.RegisterRequest;
import codesquad.application.handler.StaticResourceHandler;
import codesquad.application.processor.ArgumentResolver;
import codesquad.application.processor.HandlerRegistry;
import codesquad.application.processor.HttpRequestDispatcher;
import codesquad.csvdb.CsvDriver;
import codesquad.csvdb.jdbc.CsvFileManager;
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
import java.util.List;

public class Main {

    static {
        new CsvDriver();
    }

    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        // H2 사용할 때에는 아래 코드 사용할 것
//        DatabaseConfig databaseConfig = new DatabaseConfig("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
//        CompletableFuture.runAsync(() -> H2Console.main(databaseConfig));
        DatabaseConfig databaseConfig = new DatabaseConfig("jdbc:csvdb:dd", "sa", "");
        CsvFileManager.createTable("users", List.of("user_id", "username", "password", "email", "nickname", "created_at"));
        CsvFileManager.createTable("posts", List.of("post_id", "user_id", "content", "image_path", "created_at"));
        CsvFileManager.createTable("comments", List.of("comment_id", "post_id", "user_id", "content", "created_at"));

        HandlerRegistry handlerRegistry = new HandlerRegistry(new ArrayList<>());
        HttpRequestParser requestParser = new HttpRequestParser();

        // User DB
        UserDao userDao = new UserDaoImpl(databaseConfig);

        // Post DB
        PostDao postDao = new PostDaoImpl(databaseConfig);

        // Comment DB
        CommentDao commentDao = new CommentDaoImpl(databaseConfig);


        // 사용자 API
        registerUserApi(handlerRegistry, userDao);

        // 기본 리소스 핸들러
        StaticResourceHandler<Void, Void> defaultResourceHandler = new StaticResourceHandler<>();
        HttpResponseSerializer httpResponseSerializer = new HttpResponseSerializer();
        HttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponseSerializer);

        // 이미지 리소스 핸들러
        registerImageHandler(handlerRegistry);

        // Post
        registerPostApi(handlerRegistry, userDao, postDao, commentDao);

        // Comment
        registerCommentApi(handlerRegistry, commentDao);

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
        SecurePathManager.addSecurePath("/api/posts", HttpMethod.POST);
        SecurePathManager.addSecurePath("/api/comments/{postId}", HttpMethod.POST);


        try {
            serverInitializer.startServer(8080, httpRequestProcessor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void registerImageHandler(
            HandlerRegistry handlerRegistry
    ) {
        ImageResourceHandler imageHandler = new ImageResourceHandler();
        handlerRegistry.registerHandler(HttpMethod.GET, "/images/{filename}", imageHandler, o -> null);
    }

    private static void registerUserApi(
            HandlerRegistry handlerRegistry,
            UserDao userDao
    ) {
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
    }

    private static void registerPostApi(
            HandlerRegistry handlerRegistry,
            UserDao userDao,
            PostDao postDao,
            CommentDao commentDao
    ) {
        PostCreateLogic postCreateLogic = new PostCreateLogic(userDao, postDao);
        ArgumentResolver<PostCreateRequest> postCreateArgumentResolver = new PostCreateArgumentResolver();
        PostCreateRequestHandler postCreateRequestHandler = new PostCreateRequestHandler(postCreateArgumentResolver);
        handlerRegistry.registerHandler(HttpMethod.POST, "/api/posts", postCreateRequestHandler, postCreateLogic);

        GetPostListLogic getPostListLogic = new GetPostListLogic(postDao, commentDao);
        GetPostListRequestHandler getPostListRequestHandler = new GetPostListRequestHandler();
        handlerRegistry.registerHandler(HttpMethod.GET, "/api/posts", getPostListRequestHandler, getPostListLogic);

    }

    private static void registerCommentApi(
            HandlerRegistry handlerRegistry,
            CommentDao commentDao
    ) {
        CreateCommentLogic createCommentLogic = new CreateCommentLogic(commentDao);
        ArgumentResolver<CreateCommentRequest> argumentResolver = new CreateCommentArgumentResolver();
        CreateCommentRequestHandler createCommentRequestHandler = new CreateCommentRequestHandler(argumentResolver);
        handlerRegistry.registerHandler(HttpMethod.POST, "/api/comments/{postId}", createCommentRequestHandler, createCommentLogic);
    }

}
