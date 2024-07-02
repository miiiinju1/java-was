package codesquad;

import codesquad.handler.ConnectionHandler;
import codesquad.handler.ResourceHandler;
import codesquad.http.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        ConnectionHandler connectionHandler = new ConnectionHandler(new HttpHandler(), new ResourceHandler());
        log.debug("Listening for connection on port 8080 ....");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(() -> {
                    try (clientSocket) {
                        connectionHandler.handleConnection(clientSocket);
                    } catch (IOException e) {
                        log.error("Failed to handle connection", e);
                    }
                });
            }
            catch (IOException e) {
                log.error("Failed to accept connection", e);
            }
        }
    }
}
