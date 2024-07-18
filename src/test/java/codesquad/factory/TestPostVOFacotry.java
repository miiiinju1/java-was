package codesquad.factory;

import codesquad.application.database.vo.PostVO;
import codesquad.application.helper.Base64Util;

public class TestPostVOFacotry {

        public static PostVO createDefaultPostVO(Long userId) {
            return new PostVO(
                    null,
                    userId,
                    Base64Util.encode("content"),
                    "imagePath",
                    null
            );
        }

        public static PostVO createBy(
                Long userId,
                String content,
                String imageName
        ) {
            return new PostVO(
                    null,
                    userId,
                    content,
                    imageName,
                    null
            );
        }
}
