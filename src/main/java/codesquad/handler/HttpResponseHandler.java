package codesquad.handler;

import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.HttpVersion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpResponseHandler {

    public void writeResponse(Socket socket, HttpResponse httpResponse) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(buildHttpResponse(httpResponse));
        outputStream.flush();
    }

    private byte[] buildHttpResponse(HttpResponse httpResponse) throws IOException {
        HttpVersion httpVersion = httpResponse.getHttpVersion();
        HttpStatus httpStatus = httpResponse.getHttpStatus();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // HTTP/1.1 200 OK와 같이 응답 라인을 만들어서 byte 배열에 추가
        String responseLine = String.format("%s %d %s%s",
                httpVersion.getVersion(),
                httpStatus.getStatusCode(),
                httpStatus.getStatusMessage(),
                System.lineSeparator());

        byteArrayOutputStream.write(responseLine.getBytes(StandardCharsets.UTF_8));


        // 응답 헤더를 만들어서 byte 배열에 추가
        for (Map.Entry<String, String> headerEntry : httpResponse.getHeaders().entrySet()) {
            String header = String.format("%s: %s%s",
                    headerEntry.getKey(),
                    headerEntry.getValue(),
                    System.lineSeparator());

            byteArrayOutputStream.write(header.getBytes(StandardCharsets.UTF_8));
        }

        // 응답 바디를 byte 배열에 추가
        byteArrayOutputStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write(httpResponse.getBody().toByteArray());

        return byteArrayOutputStream.toByteArray();
    }
}
