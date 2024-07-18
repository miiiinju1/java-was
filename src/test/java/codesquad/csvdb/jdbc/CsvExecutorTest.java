package codesquad.csvdb.jdbc;

import codesquad.csvdb.engine.SQLParserKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CsvExecutorTest {

    private String tableName = "test";
    private String joinTableName = "joinTest";

    @AfterEach
    void tearDown() {
        File file = new File(tableName + ".csv");
        if (file.exists()) {
            file.delete();
        }

        File joinFile = new File(joinTableName + ".csv");
        if (joinFile.exists()) {
            joinFile.delete();
        }
    }

    @DisplayName("기본적인 insert문을 실행할 수 있다.")
    @Test
    void testMethodNameHere() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("name", "age");
        CsvFileManager.createTable(tableName, rows);
        ArrayList<String> values1 = new ArrayList<>();
        values1.add("keesun");
        values1.add("30");

        ArrayList<String> values2 = new ArrayList<>();
        values2.add("iam");
        values2.add("30");


        Map<SQLParserKey, Object> sqlParser = Map.of(
                SQLParserKey.TABLE, tableName,
                SQLParserKey.COLUMNS, List.of("id", "name", "age"),
                SQLParserKey.VALUES, List.of(
                        values1,
                        values2
                )
        );

        // when
        ResultSet execute = CsvExecutor.getGeneratedKeyAndInsert(sqlParser);

        // then
        try {
            assertThat(execute.next()).isTrue();
            long generatedKey = execute.getLong("GENERATED_KEY");
            assertThat(generatedKey).isEqualTo(1);
            assertThat(execute.next()).isTrue();
            generatedKey = execute.getLong("GENERATED_KEY");
            assertThat(generatedKey).isEqualTo(2);
            assertThat(execute.next()).isFalse();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        // Generated된 Key까지 전부 할당되어 반환된다.
        List<Map<String, String>> readTable = CsvFileManager.readTable(tableName);
        assertThat(readTable)
                .hasSize(2)
                .containsExactlyElementsOf(List.of(
                        Map.of("id", "1", "name", "keesun", "age", "30"),
                        Map.of("id", "2", "name", "iam", "age", "30")
                ));

    }

    @DisplayName("조인을 포함한 select문을 실행할 수 있다.")
    @Test
    void joinSelect() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");
        CsvFileManager.createTable(tableName, rows);

        String joinTableName = "joinTest";
        List<String> joinRows = List.of("id", "grade");
        CsvFileManager.createTable(joinTableName, joinRows);

        List<List<String>> values = List.of(
                List.of("2", "keesun", "30"),
                List.of("3", "iam", "30")
        );

        CsvFileManager.writeData(tableName, rows, values);

        List<List<String>> joinTableValues = List.of(
                List.of("2", "A"),
                List.of("3", "B")
        );

        CsvFileManager.writeData(joinTableName, joinRows, joinTableValues);

        Map<SQLParserKey, Object> sqlParser = Map.of(
                SQLParserKey.DRIVING_TABLE, tableName,
                SQLParserKey.DRIVING_TABLE_ALIAS, "t",
                SQLParserKey.DRIVEN_TABLE, joinTableName,
                SQLParserKey.DRIVEN_TABLE_ALIAS, "j",
                SQLParserKey.JOIN_CONDITION_LEFT, "t.id",
                SQLParserKey.JOIN_CONDITION_RIGHT, "j.id",
                SQLParserKey.COLUMNS, List.of("t.id", "t.name", "t.age", "j.grade")
        );

        // when
        ResultSet execute = CsvExecutor.select(sqlParser);

        // then
        try {
            assertThat(execute.next()).isTrue();
            long id = execute.getLong("t.id");
            String name = execute.getString("t.name");
            int age = execute.getInt("t.age");
            String grade = execute.getString("j.grade");
            assertThat(name).isEqualTo("keesun");
            assertThat(age).isEqualTo(30);
            assertThat(grade).isEqualTo("A");
            assertThat(execute.next()).isTrue();
            id = execute.getLong("t.id");
            name = execute.getString("t.name");
            age = execute.getInt("t.age");
            grade = execute.getString("j.grade");
            assertThat(name).isEqualTo("iam");
            assertThat(age).isEqualTo(30);
            assertThat(grade).isEqualTo("B");
            assertThat(execute.next()).isFalse();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("기본적인 select 문을 실행할 수 있다.")
    @Test
    void select() throws Exception {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");
        CsvFileManager.createTable(tableName, rows);

        List<List<String>> values = List.of(
                List.of("2", "keesun", "30"),
                List.of("3", "iam", "30")
        );
        CsvFileManager.writeData(tableName, rows, values);

        Map<SQLParserKey, Object> sqlParser = Map.of(
                SQLParserKey.DRIVING_TABLE, tableName,
                SQLParserKey.COLUMNS, List.of("id", "name", "age")
        );

        // when
        ResultSet execute = CsvExecutor.select(sqlParser);

        // then
        try {
            assertThat(execute.next()).isTrue();
            long id = execute.getLong("id");
            String name = execute.getString("name");
            int age = execute.getInt("age");
            assertThat(id).isEqualTo(2);
            assertThat(name).isEqualTo("keesun");
            assertThat(age).isEqualTo(30);
            assertThat(execute.next()).isTrue();
            id = execute.getLong("id");
             name = execute.getString("name");
             age = execute.getInt("age");
            assertThat(id).isEqualTo(3);
            assertThat(name).isEqualTo("iam");
            assertThat(age).isEqualTo(30);
            assertThat(execute.next()).isFalse();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("테이블을 필터링하여 읽어올 수 있다.")
    @Test
    void filterData() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");
        CsvFileManager.createTable(tableName, rows);

        List<List<String>> values = List.of(
                List.of("2", "keesun", "30"),
                List.of("3", "iam", "30")
        );
        CsvFileManager.writeData(tableName, rows, values);

        List<Map<String, String>> readData = CsvFileManager.readTable(tableName);
        List<String> selectedColumns = List.of("id", "name");
        String whereClause = "age = 30";

        // when
        List<Map<String, String>> results = CsvExecutor.filterData(readData, selectedColumns, whereClause);

        // then
        assertThat(results)
                .hasSize(2);
        assertThat(results.get(0))
                .containsExactlyInAnyOrderEntriesOf(Map.of("id", "2", "name", "keesun"));
        assertThat(results.get(1))
                .containsExactlyInAnyOrderEntriesOf(Map.of("id", "3", "name", "iam"));
    }

    @DisplayName("테이블을 필터링하여 읽어올 수 있다. 2")
    @Test
    void filterData2() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");
        CsvFileManager.createTable(tableName, rows);

        List<List<String>> values = List.of(
                List.of("2", "keesun", "30"),
                List.of("3", "iam", "25")
        );
        CsvFileManager.writeData(tableName, rows, values);

        List<Map<String, String>> readData = CsvFileManager.readTable(tableName);
        List<String> selectedColumns = List.of("id", "name");
        String whereClause = "age = 25";

        // when
        List<Map<String, String>> results = CsvExecutor.filterData(readData, selectedColumns, whereClause);

        // then
        assertThat(results)
                .hasSize(1);
        assertThat(results.get(0))
                .containsExactlyInAnyOrderEntriesOf(Map.of("id", "3", "name", "iam"));
    }

}