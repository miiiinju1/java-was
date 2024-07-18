package codesquad.application.config;

import codesquad.application.database.DatabaseConfig;
import codesquad.csvdb.CsvDriver;
import codesquad.csvdb.jdbc.CsvFileManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CSVTestDatabaseConfig extends DatabaseConfig {

    static {
        new CsvDriver();
    }

    public CSVTestDatabaseConfig() {
        super("jdbc:csvdb:dd", "sa", "");
        initializeDatabase();
//        resetDatabase();
    }

    private void initializeDatabase() {
        CsvFileManager.createTable("users", List.of("user_id", "username", "password", "email", "nickname", "created_at"));
        CsvFileManager.createTable("posts", List.of("post_id", "user_id", "content", "image_path", "created_at"));
        CsvFileManager.createTable("comments", List.of("comment_id", "post_id", "user_id", "content", "created_at"));
    }

    public void resetDatabase() {
        try (Connection conn = getConnection()) {
            conn.prepareStatement("DROP TABLE IF EXISTS comments;").executeQuery();
            conn.prepareStatement("DROP TABLE IF EXISTS posts;").executeQuery();
            conn.prepareStatement("DROP TABLE IF EXISTS users;").executeQuery();
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
