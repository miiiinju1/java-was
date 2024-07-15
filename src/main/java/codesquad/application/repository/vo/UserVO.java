package codesquad.application.repository.vo;

import java.time.LocalDateTime;

public record UserVO(
        Long userId,
        String username,
        String password,
        String name,
        String email,
        LocalDateTime createdAt
) {
}
