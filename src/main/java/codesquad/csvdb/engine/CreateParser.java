package codesquad.csvdb.engine;

import java.util.*;
import java.util.regex.*;

public class CreateParser implements SQLParser {

    private final Pattern CREATE_PATTERN = Pattern.compile(
            "CREATE\\s+TABLE\\s+(IF\\s+NOT\\s+EXISTS\\s+)?([^\\s]+)\\s*\\((.*)\\)",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Override
    public Map<SQLParserKey, Object> parse(String sql) {
        // Trim the SQL and check for the semicolon
        sql = sql.trim();
        if (!sql.endsWith(";")) {
            throw new IllegalArgumentException("SQL은 세미콜론(;)으로 끝나야합니다.");
        }

        // Remove the semicolon at the end
        sql = sql.substring(0, sql.length() - 1).trim();

        Map<SQLParserKey, Object> parsedResult = new HashMap<>();
        parsedResult.put(SQLParserKey.COMMAND, "CREATE");
        Matcher matcher = CREATE_PATTERN.matcher(sql);
        if (matcher.find()) {
            parsedResult.put(SQLParserKey.TABLE, matcher.group(2));
            parsedResult.put(SQLParserKey.COLUMNS, parseColumns(matcher.group(3)));
        }
        return parsedResult;
    }

    private List<String> parseColumns(String columnsStr) {
        List<String> columns = new ArrayList<>();
        StringBuilder columnBuilder = new StringBuilder();
        int parenthesesCount = 0;

        for (char ch : columnsStr.toCharArray()) {
            if (ch == ',' && parenthesesCount == 0) {
                columns.add(columnBuilder.toString().trim());
                columnBuilder.setLength(0);
            } else {
                columnBuilder.append(ch);
                if (ch == '(') {
                    parenthesesCount++;
                } else if (ch == ')') {
                    parenthesesCount--;
                }
            }
        }

        columns.add(columnBuilder.toString().trim());
        return columns;
    }

}
