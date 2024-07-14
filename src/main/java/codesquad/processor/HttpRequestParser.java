package codesquad.processor;

import codesquad.http.HttpRequest;
import codesquad.http.HttpVersion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;

public class HttpRequestParser {

    private static final String CHARSET = "UTF-8";

    private static final String HEADER_END = "\r\n\r\n";

    public HttpRequest parseRequest(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();

        boolean headerEnd = false;

        // Read until the end of headers (\r\n\r\n)
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            headerBuffer.write(buffer, 0, bytesRead);
            String currentHeaders = headerBuffer.toString(CHARSET);
            if (currentHeaders.contains(HEADER_END)) {
                headerEnd = true;
                break;
            }
        }

        if (!headerEnd) {
            throw new IllegalArgumentException("Invalid HTTP request: no end of headers found");
        }

        byte[] headerBytes = headerBuffer.toByteArray();
        String headers = new String(headerBytes, CHARSET);
        String contentType = getContentType(headers);

        int headerEndIndex = headers.indexOf(HEADER_END) + 4;
        byte[] remainingBody = Arrays.copyOfRange(headerBytes, headerEndIndex, headerBytes.length);

        // Read request line
        String[] requestLines = headers.split("\r\n");
        String requestLine = requestLines[0];
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("Request line is null");
        }

        String[] requestLineParts = requestLine.split(" ");
        if (requestLineParts.length < 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }
        String method = requestLineParts[0];
        String path = requestLineParts[1];
        String version = requestLineParts[2].trim(); // 트림 추가

        // HTTP 버전 파싱
        HttpVersion httpVersion = HttpVersion.of(version);

        // Read headers
        Map<String, List<String>> headerMap = new HashMap<>();
        int i = 1;
        while (i < requestLines.length && !requestLines[i].isEmpty()) {
            int colonIndex = requestLines[i].indexOf(":");
            if (colonIndex == -1) {
                throw new IllegalArgumentException("Invalid header line: " + requestLines[i]);
            }
            String key = requestLines[i].substring(0, colonIndex).trim();
            String valuesString = requestLines[i].substring(colonIndex + 1).trim();
            String[] values = valuesString.split(";");

            List<String> subList = headerMap.computeIfAbsent(key, k -> new ArrayList<>());
            for (String value : values) {
                subList.add(value.trim());
            }
            i++;
        }

        // Read body
        int contentLength = headerMap.containsKey("Content-Length") ? Integer.parseInt(headerMap.get("Content-Length").get(0)) : 0;
        ByteArrayOutputStream bodyBuffer = new ByteArrayOutputStream();

        if (remainingBody.length > 0) {
            bodyBuffer.write(remainingBody, 0, Math.min(remainingBody.length, contentLength));
        }

        int remainingBytes = contentLength - remainingBody.length;
        while (remainingBytes > 0 && (bytesRead = inputStream.read(buffer, 0, Math.min(buffer.length, remainingBytes))) != -1) {
            bodyBuffer.write(buffer, 0, bytesRead);
            remainingBytes -= bytesRead;
        }

        byte[] body = bodyBuffer.toByteArray();

        if (headerMap.containsKey("Content-Type") && headerMap.get("Content-Type").get(0).equals("application/x-www-form-urlencoded")) {
            try {
                String decodedBody = URLDecoder.decode(new String(body, CHARSET), CHARSET);
                body = decodedBody.getBytes(CHARSET);
            } catch (Exception e) {
                throw new IllegalArgumentException("인코딩 에러가 발생했습니다.");
            }
        }

        if (contentType.contains("multipart/form-data")) {
            handleMultipart(body, contentType);
        }

        return HttpRequest.builder()
                .method(method)
                .path(path)
                .version(httpVersion.getVersion())
                .headers(headerMap)
                .body(new ByteArrayInputStream(body))
                .build();
    }

    private String getContentType(String request) {
        for (String line : request.split("\r\n")) {
            if (line.startsWith("Content-Type:")) {
                return line.split(":")[1].trim();
            }
        }
        return "";
    }

    private void handleMultipart(byte[] requestData, String contentType) throws IOException {
        String boundary = "--" + contentType.split("boundary=")[1];
        byte[] boundaryBytes = boundary.getBytes(CHARSET);

        int index = indexOf(requestData, HEADER_END.getBytes(CHARSET), 0) + 4;
        byte[] body = Arrays.copyOfRange(requestData, index, requestData.length);

        int startIndex = 0;

        while (true) {
            int boundaryIndex = indexOf(body, boundaryBytes, startIndex);
            if (boundaryIndex == -1) {
                break;
            }

            int endIndex = indexOf(body, boundaryBytes, boundaryIndex + boundaryBytes.length);
            if (endIndex == -1) {
                endIndex = body.length;
            }

            byte[] partBytes = Arrays.copyOfRange(body, boundaryIndex + boundaryBytes.length, endIndex);

            // Process each part's headers and body
            int partHeaderEndIndex = indexOf(partBytes, HEADER_END.getBytes(CHARSET), 0) + 4;
            byte[] partHeaders = Arrays.copyOfRange(partBytes, 0, partHeaderEndIndex);
            byte[] partBody = Arrays.copyOfRange(partBytes, partHeaderEndIndex, partBytes.length);

//            System.out.println("Part headers: " + new String(partHeaders, CHARSET));
//            System.out.println("Part body size: " + partBody.length);

            // 추가로 파일 처리 로직을 추가할 수 있습니다.
            // 예: 파일 저장, 데이터베이스에 저장 등

            startIndex = endIndex + boundaryBytes.length;
        }
    }

    private int indexOf(byte[] array, byte[] target, int start) {
        for (int i = start; i <= array.length - target.length; i++) {
            boolean found = true;
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }
}
