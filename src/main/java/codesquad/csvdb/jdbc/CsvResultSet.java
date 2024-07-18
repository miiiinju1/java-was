package codesquad.csvdb.jdbc;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class CsvResultSet extends MyResultSet {

    private final List<Map<String, String>> data;
    private int currentIndex = -1;

    public CsvResultSet(List<Map<String, String>> data) {
        this.data = data;
    }

    @Override
    public boolean next() throws SQLException {
        if (currentIndex + 1 < data.size()) {
            currentIndex++;
            return true;
        }
        return false;
    }

    private void validateColumnLabel(String columnLabel) throws SQLException {
        if (!data.get(currentIndex).containsKey(columnLabel)) {
            throw new SQLException("해당 컬럼이 존재하지 않습니다.");
        }
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        validateColumnLabel(columnLabel);
        Object value = data.get(currentIndex).get(columnLabel);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        throw new SQLException("String으로 변환할 수 없는 값입니다.");
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        validateColumnLabel(columnLabel);
        Object value = data.get(currentIndex).get(columnLabel);
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        throw new SQLException("Boolean으로 변환할 수 없는 값입니다.");
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        String columnLabel = (String) data.get(currentIndex).keySet().toArray()[columnIndex - 1];
        return getBoolean(columnLabel);
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        String columnLabel = (String) data.get(currentIndex).keySet().toArray()[columnIndex - 1];
        return getString(columnLabel);
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        validateColumnLabel(columnLabel);
        Object value = data.get(currentIndex).get(columnLabel);
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        throw new SQLException("Int로 변환할 수 없는 값입니다.");
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        validateColumnLabel(columnLabel);
        Object value = data.get(currentIndex).get(columnLabel);
        if (value == null) {
            return 0;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        throw new SQLException("Long으로 변환할 수 없는 값입니다.");
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        validateColumnLabel(columnLabel);
        Object value = data.get(currentIndex).get(columnLabel);
        if (value == null) {
            return null;
        }
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }
        throw new SQLException("Timestamp로 변환할 수 없는 값입니다.");
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        String columnLabel = (String) data.get(currentIndex).keySet().toArray()[columnIndex - 1];
        return getInt(columnLabel);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        String columnLabel = (String) data.get(currentIndex).keySet().toArray()[columnIndex - 1];
        return getLong(columnLabel);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        String columnLabel = (String) data.get(currentIndex).keySet().toArray()[columnIndex - 1];
        return getTimestamp(columnLabel);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return data.get(currentIndex) == null;
    }
}
