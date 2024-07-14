package codesquad.application.processor.argumentresolver;

import codesquad.application.web.user.request.LoginRequest;
import codesquad.api.Request;
import codesquad.webserver.helper.RequestBodyParseHelper;

import java.util.Map;

public class LoginArgumentResolver implements ArgumentResolver<LoginRequest> {

    @Override
    public LoginRequest resolve(Request httpRequest) {
        final String bodyStr = new String(httpRequest.getBody().readAllBytes());

        final Map<String, String> bodyParameters = RequestBodyParseHelper.urlEncodedParameters(bodyStr);

        final String userId = bodyParameters.get("userId");
        final String password = bodyParameters.get("password");

        if (userId == null || password == null) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        return new LoginRequest(userId, password);
    }
}

