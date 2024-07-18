package codesquad.application.config;

import codesquad.application.database.DatabaseConfig;
import codesquad.csvdb.CsvDriver;
import codesquad.csvdb.jdbc.CsvFileManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class H2TestDatabaseConfig extends DatabaseConfig {

    private final String ddl;
    static {
        new CsvDriver();
    }

    public H2TestDatabaseConfig() {
//        super("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        super("jdbc:csvdb:dd", "sa", "");

        ddl = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/db/ddl.sql")))
        ).lines().collect(Collectors.joining("\n"));
        resetDatabase();
    }

    private void initializeDatabase() {
//        try (Connection conn = getConnection()) {
//             conn.prepareStatement(ddl).executeQuery();
//        } catch (SQLException | NullPointerException e) {
//            e.printStackTrace();
//        }
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
