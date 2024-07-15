package codesquad.application.web.post;

import java.io.ByteArrayInputStream;

public record PostRequest(
        Long userId,
        String content,
        ByteArrayInputStream image
) {
}
