package codesquad.webserver.processor;

import codesquad.api.Response;
import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.http.HttpVersion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpResponseSerializer {

    public byte[] buildHttpResponse(Response httpResponse) throws IOException {
        HttpVersion httpVersion = httpResponse.getHttpVersion();
        HttpStatus httpStatus = httpResponse.getHttpStatus();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // HTTP/1.1 200 OK와 같이 응답 라인을 만들어서 byte 배열에 추가
        String responseLine = String.format("%s %d %s%s",
                httpVersion.getVersion(),
                httpStatus.getStatusCode(),
                httpStatus.getStatusMessage(),
                System.lineSeparator());

        byteArrayOutputStream.write(responseLine.getBytes());

        // 응답 헤더를 만들어서 byte 배열에 추가
        for (Map.Entry<String, List<String>> headerEntry : httpResponse.getHttpHeaders().getValues()) {
            String key = headerEntry.getKey();
            List<String> values = headerEntry.getValue();
            String value = String.join("; ", values);

            String header = String.format("%s: %s%s",
                    key,
                    value,
                    System.lineSeparator());

            byteArrayOutputStream.write(header.getBytes());
        }

        // 응답 바디를 byte 배열에 추가
        byteArrayOutputStream.write(System.lineSeparator().getBytes());
        byteArrayOutputStream.write(httpResponse.getBody().toByteArray());

        return byteArrayOutputStream.toByteArray();
    }
}
