package codesquad.application.database;

import codesquad.csvdb.jdbc.CsvFileManager;

import java.sql.SQLException;
import java.util.List;

public class H2Console {
    public static void main(
            final DatabaseConfig databaseConfig
    ) {
        try {
            // H2 웹 서버 시작
            CsvFileManager.createTable("users", List.of("user_id", "username", "password", "email", "nickname", "created_at"));
            CsvFileManager.createTable("posts", List.of("post_id", "user_id", "content", "image_path", "created_at"));
            CsvFileManager.createTable("comments", List.of("comment_id", "post_id", "user_id", "content", "created_at"));

//            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
//            webServer.start();

            // 초기 DDL 실행
            DDLExecutor ddlExecutor = new DDLExecutor(databaseConfig);
            ddlExecutor.executeDDL("db/ddl.sql");

            // 계속 실행되도록 대기
            Thread.currentThread().join();
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
