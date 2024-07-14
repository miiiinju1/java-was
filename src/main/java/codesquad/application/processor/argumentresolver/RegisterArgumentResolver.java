package codesquad.application.processor.argumentresolver;

import codesquad.application.web.user.request.RegisterRequest;
import codesquad.api.Request;
import codesquad.webserver.helper.RequestBodyParseHelper;

import java.util.Map;

public class RegisterArgumentResolver implements ArgumentResolver<RegisterRequest> {

    @Override
    public RegisterRequest resolve(Request httpRequest) {

        Map<String, String> bodyParameters = RequestBodyParseHelper.urlEncodedParameters(new String(httpRequest.getBody().readAllBytes()));

        final String email = bodyParameters.get("email");
        final String userId = bodyParameters.get("userId");
        final String password = bodyParameters.get("password");
        final String name = bodyParameters.get("name");

        if (userId == null || password == null || name == null || email == null) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        return new RegisterRequest(email, userId, password, name);
    }

}
