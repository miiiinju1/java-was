package codesquad.webserver.processor;

import codesquad.api.Dispatcher;
import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.webserver.authorization.AuthorizationContextHolder;
import codesquad.webserver.authorization.SecurePathManager;
import codesquad.webserver.exception.BadRequestException;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpVersion;
import codesquad.webserver.middleware.MiddleWareChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static codesquad.webserver.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class HttpRequestProcessor {

    private final HttpRequestParser httpRequestParser;
    private final Dispatcher requestDispatcher;
    private final HttpResponseWriter httpResponseWriter;

    private final MiddleWareChain middleWareChain;
    private static final Logger log = LoggerFactory.getLogger(HttpRequestProcessor.class);

    public void process(final Socket clientSocket) throws IOException {
        final InputStream requestStream = clientSocket.getInputStream();
        try {
            final Request httpRequest = httpRequestParser.parseRequest(requestStream);
            final Response httpResponse = new HttpResponse(HttpVersion.HTTP_1_1);
            forwarding(clientSocket, httpRequest, httpResponse);
        } catch (BadRequestException e) {
            log.error("Processor : {}", e.getMessage());
            httpResponseWriter.writeResponse(clientSocket, HttpResponse.badRequestOf());
        } finally {
            AuthorizationContextHolder.clearContext();
        }
    }

    private void forwarding(final Socket clientSocket, final Request httpRequest, final Response httpResponse) throws IOException {
        middleWareChain.applyMiddleWares(httpRequest, httpResponse);

        // 만약 권한이 필요한데 권한이 없는 경우 401을 반환하고 종료
        if (SecurePathManager.isSecurePath(httpRequest.getPath(), httpRequest.getMethod()) && !AuthorizationContextHolder.isAuthorized()) {
            httpResponseWriter.writeResponse(clientSocket, HttpResponse.unauthorizedOf(""));
            return;
        }

        try {
            requestDispatcher.handleRequest(httpRequest, httpResponse);
            httpResponseWriter.writeResponse(clientSocket, httpResponse);
        } catch (Exception e) {
            log.error("Processor : {}", e.getMessage());
            switch (httpResponse.getHttpStatus()) {
                case NOT_FOUND:
                    httpResponseWriter.writeResponse(clientSocket, HttpResponse.notFoundOf(httpRequest.getPath().getBasePath()));
                    break;
                case INTERNAL_SERVER_ERROR:
                    httpResponseWriter.writeResponse(clientSocket, HttpResponse.internalServerErrorOf(httpRequest.getPath().getBasePath()));
                    break;
                default:
                    httpResponse.setStatus(INTERNAL_SERVER_ERROR);
                    httpResponseWriter.writeResponse(clientSocket, httpResponse);
                    break;
            }
        }
    }

    public HttpRequestProcessor(HttpRequestParser httpRequestParser, Dispatcher requestDispatcher, HttpResponseWriter httpResponseWriter, MiddleWareChain middleWareChain) {
        this.httpRequestParser = httpRequestParser;
        this.requestDispatcher = requestDispatcher;
        this.httpResponseWriter = httpResponseWriter;
        this.middleWareChain = middleWareChain;
    }
}
