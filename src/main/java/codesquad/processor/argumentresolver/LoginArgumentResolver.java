package codesquad.processor.argumentresolver;

import codesquad.helper.RequestBodyParseHelper;
import codesquad.http.HttpRequest;
import codesquad.web.user.request.LoginRequest;

import java.util.Map;

public class LoginArgumentResolver implements ArgumentResolver<LoginRequest> {

    @Override
    public LoginRequest resolve(HttpRequest httpRequest) {
        final String bodyStr = httpRequest.getBody();

        final Map<String, String> bodyParameters = RequestBodyParseHelper.urlEncodedParameters(bodyStr);

        final String userId = bodyParameters.get("userId");
        final String password = bodyParameters.get("password");

        if (userId == null || password == null) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        return new LoginRequest(userId, password);
    }
}

