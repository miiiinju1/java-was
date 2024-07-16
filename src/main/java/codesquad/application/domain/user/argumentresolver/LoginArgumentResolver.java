package codesquad.application.domain.user.argumentresolver;

import codesquad.application.domain.user.request.LoginRequest;
import codesquad.api.Request;
import codesquad.application.processor.ArgumentResolver;
import codesquad.webserver.helper.RequestBodyParseHelper;

import java.util.Map;

public class LoginArgumentResolver implements ArgumentResolver<LoginRequest> {

    @Override
    public LoginRequest resolve(Request httpRequest) {
        final String bodyStr = new String(httpRequest.getBody().readAllBytes());

        final Map<String, String> bodyParameters = RequestBodyParseHelper.urlEncodedParameters(bodyStr);

        final String username = bodyParameters.get("username");
        final String password = bodyParameters.get("password");

        if (username == null || password == null) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        return new LoginRequest(username, password);
    }
}

