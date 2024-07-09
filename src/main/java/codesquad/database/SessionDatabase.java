package codesquad.database;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase {

    // TODO 이후에 Long이 아니라 Session 객체를 저장하도록 변경하고, Session 객체에는 timeout 시간을 저장하도록 변경해야 함
    // TODO 지금 당장은 PK만 저장하도록
    private static final Map<String, Long> registry = new ConcurrentHashMap<>();

    public static String save(Long value) {
        String key = UUID.randomUUID().toString();
        registry.put(key, value);
        return key;
    }

    public static Long find(String key) {
        return registry.get(key);
    }

    public static void delete(String key) {
        registry.remove(key);
    }

    public static boolean containsKey(String key) {
        return registry.containsKey(key);
    }

    private SessionDatabase() {
    }

}
