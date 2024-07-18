package codesquad.application.config;

import codesquad.application.database.DatabaseConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

public class H2TestDatabaseConfig extends DatabaseConfig {

    private final String ddl;

    public H2TestDatabaseConfig() {
        super("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        ddl = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/db/ddl.sql")))
        ).lines().collect(Collectors.joining("\n"));
        resetDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(ddl);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void resetDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS comments");
            stmt.execute("DROP TABLE IF EXISTS posts");
            stmt.execute("DROP TABLE IF EXISTS users");
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}