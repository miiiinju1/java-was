package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.Mime;
import codesquad.http.header.HeaderConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ResourceHandler implements HttpHandler {

    private static final String STATIC_PATH = "static";
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws Exception {
        final String filePath = request.getPath().getValue();

        ClassLoader classLoader = ResourceHandler.class.getClassLoader();


        // 자원 스트림을 가져오고 읽어서 body에 쓰기
        try (InputStream inputStream = getResourceStream(classLoader, filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getBody().write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("리소스 파일을 읽는 중 오류 발생: {}", filePath, e);
            throw e;
        }

        // MIME 타입 설정
        Mime mime = Mime.ofFilePath(filePath);
        response.getHttpHeaders()
                .addHeader(HeaderConstants.CONTENT_TYPE, mime.getType());
    }

    private InputStream getResourceStream(final ClassLoader classLoader, String filePath) {
        final String pathPart = filePath.substring(filePath.lastIndexOf('/') + 1);

        if (pathPart.contains(".")) {
            // 마지막 슬래시 이후 부분에 확장자가 있는 경우 파일로 간주
            InputStream resourceStream = classLoader.getResourceAsStream(STATIC_PATH + filePath);
            if (resourceStream == null) {
                throw new RuntimeException("파일을 찾을 수 없습니다.");
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
            throw new RuntimeException("index.html 파일을 찾을 수 없습니다.");
        }
        return resourceStream;
    }
}
