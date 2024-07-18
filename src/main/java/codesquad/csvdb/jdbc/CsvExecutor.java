package codesquad.csvdb.jdbc;

import codesquad.csvdb.engine.SQLParserKey;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CsvExecutor {

    private static final ConcurrentHashMap<String, AtomicLong> autoIncrementMap = new ConcurrentHashMap<>();

    public static ResultSet execute(Map<SQLParserKey, Object> parsed) throws IOException {
        String command = (String) parsed.get(SQLParserKey.COMMAND);

        switch (command) {
            case "SELECT" -> {
                return select(parsed);
            }
            case "INSERT" -> {
                return getGeneratedKeyAndInsert(parsed);
            }
            case "DROP" -> {
                String tableName = (String) parsed.get(SQLParserKey.TABLE);
                CsvFileManager.dropTable(tableName);
                return null;
            }
            case "CREATE" -> {
                String tableName = (String) parsed.get(SQLParserKey.TABLE);
                List<String> columns = (List<String>) parsed.get(SQLParserKey.COLUMNS);
                CsvFileManager.createTable(tableName, columns);
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static ResultSet getGeneratedKeyAndInsert(Map<SQLParserKey, Object> parsed) throws IOException {
        String tableName = (String) parsed.get(SQLParserKey.TABLE);
        List<String> columns = (List<String>) parsed.get(SQLParserKey.COLUMNS);
        List<List<String>> values = (List<List<String>>) parsed.get(SQLParserKey.VALUES);

        AtomicLong autoIncrement = autoIncrementMap.computeIfAbsent(tableName, k -> new AtomicLong(0));
        long generatedKey = autoIncrement.incrementAndGet();

        values.forEach(value -> value.add(String.valueOf(generatedKey)));

        CsvFileManager.writeData(tableName, columns, values);

        // Creating ResultSet for generated keys
        List<Map<String, String>> resultData = new ArrayList<>();
        resultData.add(Map.of(("GENERATED_KEY"), String.valueOf(generatedKey)));
        return new CsvResultSet(resultData);
    }
    @SuppressWarnings("unchecked")
    public static ResultSet select(Map<SQLParserKey, Object> parsed) {
        String tableName = (String) parsed.get(SQLParserKey.DRIVING_TABLE);
        String tableAlias = (String) parsed.get(SQLParserKey.DRIVING_TABLE_ALIAS);
        List<String> columns = (List<String>) parsed.get(SQLParserKey.COLUMNS);
        String whereClause = (String) parsed.get(SQLParserKey.WHERE);
        String joinTable = (String) parsed.get(SQLParserKey.DRIVEN_TABLE);
        String joinTableAlias = (String) parsed.get(SQLParserKey.DRIVEN_TABLE_ALIAS);
        String joinConditionLeft = (String) parsed.get(SQLParserKey.JOIN_CONDITION_LEFT);
        String joinConditionRight = (String) parsed.get(SQLParserKey.JOIN_CONDITION_RIGHT);

        List<Map<String, String>> drivingTableData = CsvFileManager.readTable(tableName);

        if (joinTable != null) {
            List<Map<String, String>> joinTableData = CsvFileManager.readTable(joinTable);
            drivingTableData = joinTables(drivingTableData, joinTableData, joinConditionLeft, joinConditionRight, tableAlias, joinTableAlias);
        }

        List<Map<String, String>> results = filterData(drivingTableData, columns, whereClause);

        return new CsvResultSet(results);
    }

    private static List<Map<String, String>> joinTables(
            List<Map<String, String>> baseTable,
            List<Map<String, String>> joinTable,
            String joinConditionLeft,
            String joinConditionRight,
            String baseTableAlias,
            String joinTableAlias
    ) {
        return baseTable.stream()
                .flatMap(baseRow -> joinTable.stream()
                        .filter(joinRow -> baseRow.get(baseTableAlias + "." + joinConditionLeft).equals(joinRow.get(joinTableAlias + "." + joinConditionRight)))
                        .map(joinRow -> {
                            Map<String, String> combinedRow = new HashMap<>(baseRow);
                            joinRow.forEach((key, value) -> combinedRow.put(joinTableAlias + "." + key, value));
                            return combinedRow;
                        }))
                .toList();
    }


    public static List<Map<String, String>> filterData(List<Map<String, String>> data, List<String> selectedColumns, String whereClause) {
        return data.stream()
                .filter(row -> whereClause == null || evaluateWhereClause(row, whereClause))
                .map(row -> {
                    Map<String, String> selectedRow = new HashMap<>();
                    for (String column : selectedColumns) {
                        if (column.contains(".")) {
                            String[] parts = column.split("\\.");
                            String alias = parts[0];
                            String colName = parts[1];
                            selectedRow.put(column, row.getOrDefault(alias + "." + colName, row.get(colName)));
                        } else {
                            selectedRow.put(column, row.get(column));
                        }
                    }
                    return selectedRow;
                })
                .toList();
    }

    private static boolean evaluateWhereClause(Map<String, String> row, String whereClause) {
        // 단순한 where 절 평가 (예: "age > 30")
        String[] parts = whereClause.split(" ");
        if (parts.length != 3) {
            return false;
        }

        String column = parts[0];
        String operator = parts[1];
        String value = parts[2];

        if (!row.containsKey(column)) {
            return false;
        }

        String columnValue = row.get(column);

        return switch (operator) {
            case ">" -> Integer.parseInt(columnValue) > Integer.parseInt(value);
            case "<" -> Integer.parseInt(columnValue) < Integer.parseInt(value);
            case "=" -> columnValue.equals(value);
            case ">=" -> Integer.parseInt(columnValue) >= Integer.parseInt(value);
            case "<=" -> Integer.parseInt(columnValue) <= Integer.parseInt(value);
            default -> false;
        };
    }
}
