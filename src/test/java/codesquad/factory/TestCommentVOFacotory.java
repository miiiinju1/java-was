package codesquad.factory;

import codesquad.application.database.vo.CommentVO;

public class TestCommentVOFacotory {

        public static CommentVO createDefaultCommentVO(Long userId, Long postId) {
            return new CommentVO(
                    null,
                    userId,
                    postId,
                    "content",
                    null
            );
        }

        public static CommentVO createBy(
                Long userId,
                Long postId,
                String content
        ) {
            return new CommentVO(
                    null,
                    userId,
                    postId,
                    content,
                    null
            );
        }
}
