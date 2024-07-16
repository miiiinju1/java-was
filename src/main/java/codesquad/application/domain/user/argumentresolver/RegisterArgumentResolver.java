package codesquad.application.domain.user.argumentresolver;

import codesquad.application.domain.user.request.RegisterRequest;
import codesquad.api.Request;
import codesquad.application.processor.ArgumentResolver;
import codesquad.webserver.helper.RequestBodyParseHelper;

import java.util.Map;

public class RegisterArgumentResolver implements ArgumentResolver<RegisterRequest> {

    @Override
    public RegisterRequest resolve(Request httpRequest) {

        Map<String, String> bodyParameters = RequestBodyParseHelper.urlEncodedParameters(new String(httpRequest.getBody().readAllBytes()));

        final String email = bodyParameters.get("email");
        final String username = bodyParameters.get("username");
        final String password = bodyParameters.get("password");
        final String nickname = bodyParameters.get("nickname");

        if (username == null || password == null || nickname == null || email == null) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        return new RegisterRequest(email, username, password, nickname);
    }

}
