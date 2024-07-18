package codesquad.csvdb.engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ParserTest {

    @DisplayName("SELECT문 without JOIN and WHERE")
    @Test
    void testParseSelectWithoutJoinAndWhere() {
        // given
        String sql = "SELECT id, name FROM users;";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "SELECT",
                SQLParserKey.COLUMNS, List.of("id", "name"),
                SQLParserKey.DRIVING_TABLE, "users"
        ));
    }

    @DisplayName("SELECT문 without JOIN and WHERE 별칭 테스트")
    @Test
    void testParseSelectWithoutJoinAndWhereWithAlias() {
        // given
        String sql = "SELECT id, name FROM users u;";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "SELECT",
                SQLParserKey.COLUMNS, List.of("id", "name"),
                SQLParserKey.DRIVING_TABLE, "users",
                SQLParserKey.DRIVING_TABLE_ALIAS, "u"
        ));
    }

    @DisplayName("SELECT문 with LEFT JOIN 별칭 테스트")
    @Test
    void testParseSelectWithJoin() {
        // given
        String sql = "SELECT u.id, u.name, p.product FROM users u LEFT JOIN products p ON u.id = p.user_id;";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "SELECT",
                SQLParserKey.COLUMNS, List.of("u.id", "u.name", "p.product"),
                SQLParserKey.DRIVING_TABLE, "users",
                SQLParserKey.DRIVING_TABLE_ALIAS, "u",
                SQLParserKey.DRIVEN_TABLE, "products",
                SQLParserKey.DRIVEN_TABLE_ALIAS, "p",
                SQLParserKey.JOIN_CONDITION_LEFT, "u.id",
                SQLParserKey.JOIN_CONDITION_RIGHT, "p.user_id"
        ));
    }

    @DisplayName("SELECT문 with LEFT JOIN and WHERE")
    @Test
    void testParseSelectWithJoinAndWhere() {
        // given
        String sql = "SELECT u.id, u.name, p.product FROM users u LEFT JOIN products p ON u.id = p.user_id WHERE u.id > 10;";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "SELECT",
                SQLParserKey.COLUMNS, List.of("u.id", "u.name", "p.product"),
                SQLParserKey.DRIVING_TABLE, "users",
                SQLParserKey.DRIVING_TABLE_ALIAS, "u",
                SQLParserKey.DRIVEN_TABLE, "products",
                SQLParserKey.DRIVEN_TABLE_ALIAS, "p",
                SQLParserKey.JOIN_CONDITION_LEFT, "u.id",
                SQLParserKey.JOIN_CONDITION_RIGHT, "p.user_id",
                SQLParserKey.WHERE, "u.id > 10"
        ));
    }

    @DisplayName("SELECT문 with WHERE")
    @Test
    void testParseSelectWithWhere() {
        // given
        String sql = "SELECT id, name FROM users WHERE age > 30;";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "SELECT",
                SQLParserKey.COLUMNS, List.of("id", "name"),
                SQLParserKey.DRIVING_TABLE, "users",
                SQLParserKey.WHERE, "age > 30"
        ));
    }


    @DisplayName("CREATE문")
    @Test
    void testParseCreate() {
        // given
        String sql = "CREATE TABLE IF NOT EXISTS users (user_id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL, email VARCHAR(100) NOT NULL UNIQUE, nickname VARCHAR(100) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "CREATE",
                SQLParserKey.TABLE, "users",
                SQLParserKey.COLUMNS, List.of(
                        "user_id INT PRIMARY KEY AUTO_INCREMENT",
                        "username VARCHAR(50) NOT NULL UNIQUE",
                        "password VARCHAR(255) NOT NULL",
                        "email VARCHAR(100) NOT NULL UNIQUE",
                        "nickname VARCHAR(100) NOT NULL",
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        ));
    }


    @DisplayName("DROP문")
    @Test
    void testParseDrop() {
        // given
        String sql = "DROP TABLE users";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "DROP",
                SQLParserKey.TABLE, "users"
        ));
    }

    @DisplayName("CREATE TABLE users")
    @Test
    void testParseCreateTableUsers() {
        // given
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) NOT NULL UNIQUE, " +
                "nickname VARCHAR(100) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "CREATE",
                SQLParserKey.TABLE, "users",
                SQLParserKey.COLUMNS, List.of(
                        "user_id INT PRIMARY KEY AUTO_INCREMENT",
                        "username VARCHAR(50) NOT NULL UNIQUE",
                        "password VARCHAR(255) NOT NULL",
                        "email VARCHAR(100) NOT NULL UNIQUE",
                        "nickname VARCHAR(100) NOT NULL",
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                )
        ));
    }

    @DisplayName("CREATE TABLE posts")
    @Test
    void testParseCreateTablePosts() {
        // given
        String sql = "CREATE TABLE IF NOT EXISTS POSTS (" +
                "post_id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "user_id BIGINT NOT NULL, " +
                "content VARCHAR(255) NOT NULL, " +
                "image_path VARCHAR(255) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "CREATE",
                SQLParserKey.TABLE, "POSTS",
                SQLParserKey.COLUMNS, List.of(
                        "post_id BIGINT PRIMARY KEY AUTO_INCREMENT",
                        "user_id BIGINT NOT NULL",
                        "content VARCHAR(255) NOT NULL",
                        "image_path VARCHAR(255) NOT NULL",
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                )
        ));
    }

    @DisplayName("CREATE TABLE comments")
    @Test
    void testParseCreateTableComments() {
        // given
        String sql = "CREATE TABLE IF NOT EXISTS comments (" +
                "comment_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "post_id INT NOT NULL, " +
                "user_id INT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "CREATE",
                SQLParserKey.TABLE, "comments",
                SQLParserKey.COLUMNS, List.of(
                        "comment_id INT PRIMARY KEY AUTO_INCREMENT",
                        "post_id INT NOT NULL",
                        "user_id INT NOT NULL",
                        "content TEXT NOT NULL",
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                )
        ));
    }


    @DisplayName("CREATE TABLE users")
    @Test
    void testParseCreateTableUsers2() {
        // given
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) NOT NULL UNIQUE, " +
                "nickname VARCHAR(100) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "CREATE",
                SQLParserKey.TABLE, "users",
                SQLParserKey.COLUMNS, List.of(
                        "user_id INT PRIMARY KEY AUTO_INCREMENT",
                        "username VARCHAR(50) NOT NULL UNIQUE",
                        "password VARCHAR(255) NOT NULL",
                        "email VARCHAR(100) NOT NULL UNIQUE",
                        "nickname VARCHAR(100) NOT NULL",
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                )
        ));
    }



    @DisplayName("INSERT문")
    @Test
    void testParseInsert() {
        // given
        String sql = "INSERT INTO users (username, password, email, nickname) VALUES ('user1', 'pass1', 'user1@example.com', 'User1')";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "INSERT",
                SQLParserKey.TABLE, "users",
                SQLParserKey.COLUMNS, List.of("username", "password", "email", "nickname"),
                SQLParserKey.VALUES,
                List.of(
                        List.of("user1", "pass1", "user1@example.com", "User1")
                )));
    }

    @DisplayName("INSERT INTO posts")
    @Test
    void testParseInsertIntoPosts() {
        // given
        String sql = "INSERT INTO posts (user_id, content, image_path) VALUES " +
                "(1, 'asdf', '3479a814-0272-49d8-8699-e318f23cc902.png');";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "INSERT",
                SQLParserKey.TABLE, "posts",
                SQLParserKey.COLUMNS, List.of("user_id", "content", "image_path"),
                SQLParserKey.VALUES, List.of(
                        List.of("1", "asdf", "3479a814-0272-49d8-8699-e318f23cc902.png")
                )
        ));
    }

    @DisplayName("INSERT INTO comments")
    @Test
    void testParseInsertIntoComments() {
        // given
        String sql = "INSERT INTO comments (post_id, user_id, content, created_at) VALUES " +
                "(1, 1, 'This is the first comment.', '2024-07-16 14:30:00'), " +
                "(1, 2, 'This is the second comment.', '2024-07-16 14:31:00'), " +
                "(1, 3, 'This is the third comment.', '2024-07-16 14:32:00');";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "INSERT",
                SQLParserKey.TABLE, "comments",
                SQLParserKey.COLUMNS, List.of("post_id", "user_id", "content", "created_at"),
                SQLParserKey.VALUES, List.of(
                        List.of("1", "1", "This is the first comment.", "2024-07-16 14:30:00"),
                        List.of("1", "2", "This is the second comment.", "2024-07-16 14:31:00"),
                        List.of("1", "3", "This is the third comment.", "2024-07-16 14:32:00")
                )
        ));
    }

    @DisplayName("INSERT INTO users")
    @Test
    void testParseInsertIntoUsers2() {
        // given
        String sql = "INSERT INTO users (username, password, email, nickname) VALUES " +
                "('d', 'd', 'd@n', 'd'), " +
                "('user1001', 'password', 'user1001@example.com', 'User 1001'), " +
                "('user1002', 'password', 'user1002@example.com', 'User 1002'), " +
                "('user1003', 'password', 'user1003@example.com', 'User 1003');";

        // when
        Map<SQLParserKey, Object> result = Parser.parseSQL(sql);

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                SQLParserKey.COMMAND, "INSERT",
                SQLParserKey.TABLE, "users",
                SQLParserKey.COLUMNS, List.of("username", "password", "email", "nickname"),
                SQLParserKey.VALUES, List.of(
                        List.of("d", "d", "d@n", "d"),
                        List.of("user1001", "password", "user1001@example.com", "User 1001"),
                        List.of("user1002", "password", "user1002@example.com", "User 1002"),
                        List.of("user1003", "password", "user1003@example.com", "User 1003")
                )
        ));
    }

}

