package codesquad.database;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

public class Database<T> {
    private final Map<Long, T> database = new ConcurrentHashMap<>();
    private AtomicLong id = new AtomicLong(0);

    public long save(T data) {
        validateValue(data);
        long key = id.incrementAndGet();
        database.put(key, data);
        return key;
    }

    public Optional<T> findById(long id) {
        if(!database.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(database.get(id));
    }

    public void delete(long id) {
        validateId(id);
        database.remove(id);
    }

    public void update(long id, T data) {
        validateId(id);
        validateValue(data);
        database.put(id, data);
    }

    public Collection<T> findAll() {
        return database.values();
    }

    public Collection<T> findByCondition(Predicate<T> predicate) {
        return database.values().stream()
                .filter(predicate)
                .toList();
    }

    private void validateId(long id) {
        if(!database.containsKey(id)) {
            throw new IllegalArgumentException("해당 id를 가진 데이터가 없습니다.");
        }
    }

    private void validateValue(T data) {
        if(data == null) {
            throw new IllegalArgumentException("데이터가 null입니다.");
        }
    }

}
