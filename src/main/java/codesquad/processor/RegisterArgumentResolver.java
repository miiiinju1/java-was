package codesquad.processor;

import codesquad.http.HttpRequest;
import codesquad.web.user.RegisterRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RegisterArgumentResolver implements ArgumentResolver<RegisterRequest> {

    @Override
    public RegisterRequest resolve(HttpRequest httpRequest) {

        //아직 PathVariable을 처리하는 방법은 생각하지 않았음
        // 임시로 RegisterRequest를 만들기, TODO 필요에 따라 다양하게 만들 수 있게 수정
        Map<String, String> bodyParameters = bodyParameters(httpRequest.getBody());

        final String email = bodyParameters.get("email");
        final String userId = bodyParameters.get("userId");
        final String password = bodyParameters.get("password");
        final String name = bodyParameters.get("name");

        if (userId == null || password == null || name == null || email == null) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        return new RegisterRequest(email, userId, password, name);
    }

    private Map<String, String> bodyParameters(String bodyString) {
        return Arrays.stream(bodyString.split("&"))
                .map(s -> {
                    String[] split = s.split("=");
                    String key = split[0];
                    String value = split.length > 1 ? split[1] : ""; // If no value, use empty string

                    return Map.entry(key, value);
                })

                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
