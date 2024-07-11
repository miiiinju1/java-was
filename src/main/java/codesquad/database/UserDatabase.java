package codesquad.database;

import codesquad.model.User;

import java.util.concurrent.ConcurrentHashMap;

public class UserDatabase extends Database<User> {

    private final ConcurrentHashMap<String, Object> unique = new ConcurrentHashMap<>();
    private static final Object PRESENT = new Object();

    @Override
    public long save(User user) {

        // 사용자 ID unique의 동기화 문제 해결을 위해 ConcurrentHashMap의 putIfAbsent 사용

        // 적절한 방식
        Object value = unique.putIfAbsent(user.getUserId(), PRESENT);
        if (value != null) {
            throw new IllegalArgumentException("이미 존재하는 userId 입니다.");
        }

        // 잘못된 방식
//        if(unique.containsKey(user.getUserId())) {
//            throw new IllegalArgumentException("이미 존재하는 userId 입니다.");
//        }
//        unique.put(user.getUserId(), PRESENT);
        //////////////////////////////////////////////////////
        return super.save(user);
    }

}