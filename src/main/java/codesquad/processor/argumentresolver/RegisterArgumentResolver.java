package codesquad.processor.argumentresolver;

import codesquad.helper.RequestBodyParseHelper;
import codesquad.http.HttpRequest;
import codesquad.web.user.request.RegisterRequest;

import java.util.Map;

public class RegisterArgumentResolver implements ArgumentResolver<RegisterRequest> {

    @Override
    public RegisterRequest resolve(HttpRequest httpRequest) {

        Map<String, String> bodyParameters = RequestBodyParseHelper.urlEncodedParameters(httpRequest.getBody());

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
