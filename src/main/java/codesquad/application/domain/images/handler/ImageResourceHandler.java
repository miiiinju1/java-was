package codesquad.application.domain.images.handler;

import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.application.handler.HttpHandler;
import codesquad.application.processor.Triggerable;
import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.http.Mime;
import codesquad.webserver.http.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageResourceHandler implements HttpHandler<String, Void> {

    private static final String BASE_PATH = System.getProperty("user.home") + "/uploads";

    @Override
    public void handle(Request httpRequest, Response httpResponse, Triggerable<String, Void> triggerable) throws IOException {
        Path path = httpRequest.getPath();
        String filename = path.getSegments().get(1);

        String relativePath = BASE_PATH + "/" + filename;
        File file = new File(relativePath);

        if (file.exists() && !file.isDirectory()) {
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = httpResponse.getBody()) {
                httpResponse.setStatus(HttpStatus.OK);
                Mime mime = Mime.ofFilePath(filename);
                httpResponse.setHeader("Content-Type", mime.getType());

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        } else {
            httpResponse.setStatus(HttpStatus.NOT_FOUND);
            httpResponse.setHeader("Content-Type", "text/plain");
            try (OutputStream os = httpResponse.getBody()) {
                os.write("File not found".getBytes());
            }
        }
    }
}
