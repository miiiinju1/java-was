package codesquad.handler;

import codesquad.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ConnectionHandler {

    private final HttpHandler httpHandler;
    private final ResourceHandler resourceHandler;
    private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);

    public ConnectionHandler(HttpHandler httpHandler, ResourceHandler resourceHandler) {
        this.httpHandler = httpHandler;
        this.resourceHandler = resourceHandler;
    }

    public void handleConnection(final Socket clientSocket) throws IOException {
        final InputStream requestStream = clientSocket.getInputStream();
        final OutputStream responseStream = clientSocket.getOutputStream();

        final HttpRequest httpRequest = httpHandler.parseRequest(requestStream);

        try {
            final InputStream inputStream = resourceHandler.readFileAsStream(httpRequest.getPath());
            log.debug("request : {}", httpRequest);
            sendResponse(responseStream, inputStream);
        }
        catch (IllegalArgumentException e) {
            log.error("File not found! : {}", httpRequest.getPath());
            sendNotFoundResponse(responseStream);
        }

    }

    private void sendResponse(OutputStream outputStream, InputStream fileInputStream) throws IOException {
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

    private void sendNotFoundResponse(OutputStream outputStream) throws IOException {
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
