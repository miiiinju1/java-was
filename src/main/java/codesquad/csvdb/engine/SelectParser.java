package codesquad.csvdb.engine;

import java.util.*;
import java.util.regex.*;

public class SelectParser implements SQLParser {

    private static final Pattern SELECT_PATTERN = Pattern.compile(
            "SELECT\\s+(.*?)\\s+FROM\\s+([^\\s]+)(?:\\s+(\\w+))?(?:\\s+LEFT JOIN\\s+([^\\s]+)(?:\\s+(\\w+))?\\s+ON\\s+([^\\s]+)\\s*=\\s*([^\\s]+))?",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Override
    public Map<SQLParserKey, Object> parse(String sql) {
        sql = sql.trim();
        if (!sql.endsWith(";")) {
            throw new IllegalArgumentException("SQL은 세미콜론(;)으로 끝나야합니다.");
        }

        sql = sql.substring(0, sql.length() - 1).trim();

        String[] parts = sql.split("(?i)\\s+WHERE\\s+", 2);
        Map<SQLParserKey, Object> parsedResult = new HashMap<>();
        Matcher matcher = SELECT_PATTERN.matcher(parts[0]);
        parsedResult.put(SQLParserKey.COMMAND, "SELECT");
        if (matcher.find()) {
            parsedResult.put(SQLParserKey.COLUMNS, Arrays.asList(matcher.group(1).split("\\s*,\\s*")));
            parsedResult.put(SQLParserKey.DRIVING_TABLE, matcher.group(2));
            if (matcher.group(3) != null && !matcher.group(3).equalsIgnoreCase("WHERE")) {
                parsedResult.put(SQLParserKey.DRIVING_TABLE_ALIAS, matcher.group(3));
            }
            if (matcher.group(4) != null) {
                parsedResult.put(SQLParserKey.DRIVEN_TABLE, matcher.group(4));
                if (matcher.group(5) != null) {
                    parsedResult.put(SQLParserKey.DRIVEN_TABLE_ALIAS, matcher.group(5));
                }
                parsedResult.put(SQLParserKey.JOIN_CONDITION_LEFT, matcher.group(6));
                parsedResult.put(SQLParserKey.JOIN_CONDITION_RIGHT, matcher.group(7));
            }
        }
        if (parts.length > 1) {
            parsedResult.put(SQLParserKey.WHERE, parts[1].trim());
        }
        return parsedResult;
    }

    public static void main(String[] args) {
        SelectParser parser = new SelectParser();

        String sql = "SELECT a, b FROM table1 t1 LEFT JOIN table2 t2 ON t1.id = t2.id WHERE a = 1;";
        try {
            Map<SQLParserKey, Object> result = parser.parse(sql);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        // Test with missing semicolon
        String invalidSql = "SELECT a, b FROM table1 t1 LEFT JOIN table2 t2 ON t1.id = t2.id WHERE a = 1";
        try {
            Map<SQLParserKey, Object> result = parser.parse(invalidSql);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
