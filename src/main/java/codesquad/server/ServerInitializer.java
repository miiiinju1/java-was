package codesquad.server;

import codesquad.handler.HttpRequestDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerInitializer {

    private static final Logger log = LoggerFactory.getLogger(ServerInitializer.class);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void startServer(final int port, final HttpRequestDispatcher httpRequestDispatcher) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);

        log.debug("Listening for connection on port {} ....", port);

        while (true) {
            try {
                final Socket clientSocket = serverSocket.accept();
                executorService.execute(() -> {
                    try (clientSocket) {
                        httpRequestDispatcher.handleConnection(clientSocket);
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
