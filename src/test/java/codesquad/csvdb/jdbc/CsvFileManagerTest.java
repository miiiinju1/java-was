package codesquad.csvdb.jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CsvFileManagerTest {

    private String tableName = "test";

    @AfterEach
    void tearDown() {
        File file = new File(tableName + ".csv");
        if (file.exists()) {
            file.delete();
        }
    }

    @DisplayName("새로운 테이블을 csv파일로 만들 수 있다.")
    @Test
    void createTable() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");

        // when
        CsvFileManager.createTable(tableName, rows);

        // then
        File file = new File(tableName + ".csv");
        assertThat(file.exists()).isTrue();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String headerLine = reader.readLine();
        assertThat(headerLine).isEqualTo("id,name,age");
    }

    @DisplayName("저장된 테이블에 데이터를 추가할 수 있다.")
    @Test
    void writeData() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");

        CsvFileManager.createTable(tableName, rows);

        List<List<String>> values = List.of(
                List.of("2", "keesun", "30")
        );

        // when
        CsvFileManager.writeData(tableName, rows, values);

        // then
        File file = new File(tableName + ".csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String headerLine = reader.readLine();
        assertThat(headerLine).isEqualTo("id,name,age");

        String dataLine = reader.readLine();
        assertThat(dataLine).isEqualTo("2,keesun,30");
    }

    @DisplayName("저장된 테이블에 정의되지 않은 column에는 빈 값이 들어간다.")
    @Test
    void writeDataWithEmptyValue() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");

        CsvFileManager.createTable(tableName, rows);

        List<String> addRows = List.of("id", "name");
        List<List<String>> values = List.of(
                List.of("2", "keesun")
        );

        // when
        CsvFileManager.writeData(tableName, addRows, values);

        // then
        File file = new File(tableName + ".csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String headerLine = reader.readLine();
        assertThat(headerLine).isEqualTo("id,name,age");

        String dataLine = reader.readLine();
        assertThat(dataLine).isEqualTo("2,keesun,null");
    }

    @DisplayName("저장된 테이블에 여러 데이터를 추가할 수 있다.")
    @Test
    void writeMultipleData() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");

        CsvFileManager.createTable(tableName, rows);

        List<List<String>> values = List.of(
                List.of("2", "keesun", "30"),
                List.of("3", "iam", "30")
        );

        // when
        CsvFileManager.writeData(tableName, rows, values);

        // then
        File file = new File(tableName + ".csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String headerLine = reader.readLine();
        assertThat(headerLine).isEqualTo("id,name,age");

        for (List<String> value : values) {
            String dataLine = reader.readLine();
            assertThat(dataLine).isEqualTo(String.join(",", value));
        }

    }

    @DisplayName("기존에 이미 자료가 있을 때 뒤에 추가할 수 있다.")
    @Test
    void appendData() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");
        CsvFileManager.createTable(tableName, rows);

        List<List<String>> values = List.of(
                List.of("2", "keesun", "30"),
                List.of("3", "iam", "30")
        );
        CsvFileManager.writeData(tableName, rows, values);

        List<List<String>> appendRows = List.of(
                List.of("4", "minju", "30"),
                List.of("5", "test", "30")
        );

        // when
        CsvFileManager.writeData(tableName, rows, appendRows);

        // then
        File file = new File(tableName + ".csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String headerLine = reader.readLine();
        assertThat(headerLine).isEqualTo("id,name,age");

        for (List<String> value : values) {
            String dataLine = reader.readLine();
            assertThat(dataLine).isEqualTo(String.join(",", value));
        }

        for (List<String> value : appendRows) {
            String dataLine = reader.readLine();
            assertThat(dataLine).isEqualTo(String.join(",", value));
        }
    }

    @DisplayName("테이블을 읽어올 수 있다.")
    @Test
    void readTable() throws IOException {
        // given
        String tableName = "test";
        List<String> rows = List.of("id", "name", "age");
        CsvFileManager.createTable(tableName, rows);

        List<List<String>> values = List.of(
                List.of("2", "keesun", "30"),
                List.of("3", "iam", "30")
        );
        CsvFileManager.writeData(tableName, rows, values);


        // when
        List<Map<String, String>> readValues = CsvFileManager.readTable(tableName);

        // then
        assertThat(readValues).hasSize(2);
        assertThat(readValues.get(0))
                .containsExactlyInAnyOrderEntriesOf(Map.of("id", "2", "name", "keesun", "age", "30"));
        assertThat(readValues.get(1))
                .containsExactlyInAnyOrderEntriesOf(Map.of("id", "3", "name", "iam", "age", "30"));
    }




}