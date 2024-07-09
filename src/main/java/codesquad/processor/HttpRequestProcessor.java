package codesquad.processor;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.HttpVersion;
import codesquad.middleware.MiddleWareChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HttpRequestProcessor {

    private final HttpRequestParser httpRequestParser;
    private final HttpRequestDispatcher httpRequestDispatcher;
    private final HttpResponseWriter httpResponseWriter;

    private final MiddleWareChain middleWareChain;
    private static final Logger log = LoggerFactory.getLogger(HttpRequestProcessor.class);

    public void process(final Socket clientSocket) throws IOException {
        final InputStream requestStream = clientSocket.getInputStream();
        final HttpRequest httpRequest = httpRequestParser.parseRequest(requestStream);
        final HttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_1_1);

        middleWareChain.applyMiddleWares(httpRequest, httpResponse);

        // 위에서 return하지 말고

        // 권한이 설정됐는지 검사해서

        // 권한이 없으면 401을 리턴하고 return해버리게 만들기

        // 그럼 middleWareChain은 boolean이 아니라 void로 바꿔도 됨


        try {
            httpRequestDispatcher.handleConnection(httpRequest, httpResponse);
            httpResponseWriter.writeResponse(clientSocket, httpResponse);
        }
        catch (Exception e) {
            log.error("Processor : {}", e.getMessage());
            switch (httpResponse.getHttpStatus()) {
                case NOT_FOUND:
                    httpResponseWriter.writeResponse(clientSocket, HttpResponse.notFoundOf(httpRequest.getPath().getBasePath()));
                    break;
                case INTERNAL_SERVER_ERROR:
                    httpResponseWriter.writeResponse(clientSocket, HttpResponse.internalServerErrorOf(httpRequest.getPath().getBasePath()));
                    break;
                default:
                    httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    httpResponseWriter.writeResponse(clientSocket, httpResponse);
                    break;
            }
        }
    }

    public HttpRequestProcessor(HttpRequestParser httpRequestParser, HttpRequestDispatcher httpRequestDispatcher, HttpResponseWriter httpResponseWriter, MiddleWareChain middleWareChain) {
        this.httpRequestParser = httpRequestParser;
        this.httpRequestDispatcher = httpRequestDispatcher;
        this.httpResponseWriter = httpResponseWriter;
        this.middleWareChain = middleWareChain;
    }
}
