package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.processor.ArgumentResolver;
import codesquad.processor.Triggerable;

public class ApiRequestHandlerAdapter<T, R> implements HttpHandlerAdapter<T, R> {

    private final ArgumentResolver<T> argumentResolver;

    public ApiRequestHandlerAdapter(ArgumentResolver<T> argumentResolver) {
        this.argumentResolver = argumentResolver;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse response, Triggerable<T, R> triggerable) throws Exception {

        T request = argumentResolver.resolve(httpRequest);

        R res = triggerable.run(request);

        // 결과를 response에 담아서 반환
        response.setStatus(HttpStatus.FOUND);

        response.getHttpHeaders().addHeader("Location", "/");

        // 이걸 HttpHandler에 default 메서드로 두고 필요한 경우 오버라이드해서 사용하도록 하면 어떨까?
    }
}
