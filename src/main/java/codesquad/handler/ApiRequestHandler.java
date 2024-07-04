package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.Path;
import codesquad.model.User;
import codesquad.web.user.RegisterRequest;

import java.util.Map;

public class ApiRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse response) throws Exception {
        Path path = httpRequest.getPath();

        // ArgumentResolver가 할 일을 현재는 여기에서 처리
            //아직 PathVariable을 처리하는 방법은 생각하지 않았음
        // 임시로 RegisterRequest를 만들기, TODO 필요에 따라 다양하게 만들 수 있게 수정
        Map<String, String> queryParameters = path.getQueryParameters();
        final String email = queryParameters.get("email");
        final String userId = queryParameters.get("userId");
        final String password = queryParameters.get("password");
        final String name = queryParameters.get("name");

        if (userId == null || password == null || name == null || email == null) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        RegisterRequest registerRequest = new RegisterRequest(email, userId, password, name);

        // 이후 해당 로직을 수행하는 메서드를 호출
        registerUser(registerRequest);

        // 결과를 response에 담아서 반환
        response.setHttpStatus(HttpStatus.FOUND);

        response.getHttpHeaders().addHeader("Location", "/login");

        // 이걸 HttpHandler에 default 메서드로 두고 필요한 경우 오버라이드해서 사용하도록 하면 어떨까?
    }

    // TODO 임시 비즈니스 로직
    private void registerUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String userId = registerRequest.getUserId();
        String password = registerRequest.getPassword();
        String name = registerRequest.getName();

        User user = new User(userId, password, name, email);
        System.out.println("user = " + user);

    }
}
