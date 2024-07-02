package codesquad;

import codesquad.handler.ResourceHandler;
import codesquad.http.HttpHandler;
import codesquad.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        HttpHandler httpHandler = new HttpHandler();
        ResourceHandler resourceHandler = new ResourceHandler();
        log.debug("Listening for connection on port 8080 ....");

        while (true) { // 무한 루프를 돌며 클라이언트의 연결을 기다립니다.
            try (Socket clientSocket = serverSocket.accept()) { // 클라이언트 연결을 수락합니다.
                InputStream requestStream = clientSocket.getInputStream();
                OutputStream responseStream = clientSocket.getOutputStream();

                HttpRequest httpRequest = httpHandler.parseRequest(requestStream);

                try {
                    InputStream inputStream = resourceHandler.readFileAsStream(httpRequest.getPath());
                    log.debug("request : {}", httpRequest);
                    sendResponse(responseStream, inputStream);
                }
                catch (IllegalArgumentException e) {
                    log.error("File not found! : {}", httpRequest.getPath());
                    sendNotFoundResponse(responseStream);
                }
            }
        }
    }

    private static void sendResponse(OutputStream outputStream, InputStream fileInputStream) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        bw.write("HTTP/1.1 200 OK");
        bw.newLine();
        bw.write("Content-Type: text/html; charset=UTF-8");
        bw.newLine();
        bw.newLine();
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            bw.write(line);
            bw.newLine();
        }
        bw.flush();
    }

    private static void sendNotFoundResponse(OutputStream outputStream) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        bw.write("HTTP/1.1 404 Not Found");
        bw.newLine();
        bw.write("Content-Type: text/html; charset=UTF-8");
        bw.newLine();
        bw.newLine();
        bw.write("<html><body><h1>404 Not Found</h1></body></html>");
        bw.flush();
    }
}
