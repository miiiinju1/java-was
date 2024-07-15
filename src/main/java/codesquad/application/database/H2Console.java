package codesquad.application.database;

import org.h2.tools.Server;

import java.sql.SQLException;

public class H2Console {
    public static void main(
            final DatabaseConfig databaseConfig
    ) {
        try {
            // H2 웹 서버 시작
            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
            webServer.start();

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
