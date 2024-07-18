package codesquad.csvdb.engine;

import java.util.Map;

public interface SQLParser {
    Map<SQLParserKey, Object> parse(String sql);
}
