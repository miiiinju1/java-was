package codesquad.application.handler;

import codesquad.application.processor.Triggerable;
import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.http.Mime;
import codesquad.webserver.http.header.HeaderConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class StaticResourceHandler<T, R> implements HttpHandler<T,R> {

    private static final String STATIC_PATH = "static";
    private static final Logger log = LoggerFactory.getLogger(StaticResourceHandler.class);

    @Override
    public void handle(Request httpRequest, Response httpResponse, Triggerable<T, R> triggerable) throws IOException {
        final String filePath = httpRequest.getPath().getBasePath();

        ClassLoader classLoader = StaticResourceHandler.class.getClassLoader();


        // 자원 스트림을 가져오고 읽어서 body에 쓰기
        try (InputStream inputStream = getResourceStream(classLoader, filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                httpResponse.getBody().write(buffer, 0, bytesRead);
            }
        } catch (IllegalArgumentException e) {
            httpResponse.setStatus(HttpStatus.NOT_FOUND);
            log.error("리소스 파일을 읽는 중 오류 발생: {}, {}", filePath, e.getMessage());
            throw e;
        } catch (IOException e) {
            httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            log.error("리소스 파일을 읽는 중 IO 오류 발생: {}, {}", filePath, e.getMessage());
            throw e;
        }
        // TODO 커스텀 예외를 만들어서 예외 타입에 따라 오버로딩하는 방식으로 에외처리 중앙화하기

        // MIME 타입 설정
        Mime mime = Mime.ofFilePath(filePath);
        httpResponse.setStatus(HttpStatus.OK);
        httpResponse.setHeader(HeaderConstants.CACHE_CONTROL, "public, max-age=31536000");

        httpResponse.getHttpHeaders()
                .addHeader(HeaderConstants.CONTENT_TYPE, mime.getType());
    }

    private InputStream getResourceStream(final ClassLoader classLoader, String filePath) {
        final String pathPart = filePath.substring(filePath.lastIndexOf('/') + 1);

        if (pathPart.contains(".")) {
            // 마지막 슬래시 이후 부분에 확장자가 있는 경우 파일로 간주
            InputStream resourceStream = classLoader.getResourceAsStream(STATIC_PATH + filePath);
            if (resourceStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다.");
            }
            return resourceStream;
        }
        // 마지막 슬래시 이후 부분에 확장자가 없는 경우 디렉터리로 간주하여 index.html 확인
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        filePath += "index.html";
        InputStream resourceStream = classLoader.getResourceAsStream(STATIC_PATH + filePath);
        if (resourceStream == null) {
            throw new IllegalArgumentException("index.html 파일을 찾을 수 없습니다.");
        }
        return resourceStream;
    }
}
