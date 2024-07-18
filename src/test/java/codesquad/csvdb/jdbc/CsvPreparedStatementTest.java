package codesquad.csvdb.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class CsvPreparedStatementTest {

    @DisplayName("쿼리를 컴파일한다.")
    @Test
    void compile() throws SQLException {
        // given
        String sql = "SELECT * FROM user WHERE id = ?";
        CsvPreparedStatement csvPreparedStatement = new CsvPreparedStatement(null, sql);
        csvPreparedStatement.setLong(1, 1L);

        // when
        final String str = csvPreparedStatement.compiledSql();

        // then
        assertThat(str).isEqualTo("SELECT * FROM user WHERE id = 1");

    }

    @DisplayName("쿼리를 컴파일한다.")
    @Test
    void compile2() throws SQLException {
        // given
        String sql = "SELECT * FROM user WHERE id = ? AND name = ?";
        CsvPreparedStatement csvPreparedStatement = new CsvPreparedStatement(null, sql);
        csvPreparedStatement.setLong(1, 1L);
        csvPreparedStatement.setString(2, "name");

        // when
        final String str = csvPreparedStatement.compiledSql();

        // then
        assertThat(str).isEqualTo("SELECT * FROM user WHERE id = 1 AND name = 'name'");
    }

    @DisplayName("쿼리를 컴파일한다.")
    @Test
    void compile3() throws SQLException {
        // given
        String sql = "SELECT * FROM user WHERE id = ? AND name = ? AND age = ?";
        CsvPreparedStatement csvPreparedStatement = new CsvPreparedStatement(null, sql);
        csvPreparedStatement.setLong(1, 1L);
        csvPreparedStatement.setString(2, "name");
        csvPreparedStatement.setInt(3, 20);

        // when
        final String str = csvPreparedStatement.compiledSql();

        // then
        assertThat(str).isEqualTo("SELECT * FROM user WHERE id = 1 AND name = 'name' AND age = 20");
    }

    @DisplayName("쿼리를 컴파일한다.")
    @Test
    void compile4() throws SQLException {
        // given
        String sql = "SELECT * FROM user WHERE id = ? AND name = ? AND age = ? AND email = ?";
        CsvPreparedStatement csvPreparedStatement = new CsvPreparedStatement(null, sql);
        csvPreparedStatement.setLong(1, 1L);
        csvPreparedStatement.setString(2, "name");
        csvPreparedStatement.setInt(3, 20);
        csvPreparedStatement.setString(4, "email");

        // when
        final String str = csvPreparedStatement.compiledSql();

        // then
        assertThat(str).isEqualTo("SELECT * FROM user WHERE id = 1 AND name = 'name' AND age = 20 AND email = 'email'");
    }

}