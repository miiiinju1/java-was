package codesquad.webserver.middleware;

import codesquad.api.Request;
import codesquad.api.Response;
import codesquad.webserver.authorization.AuthorizationContext;
import codesquad.webserver.authorization.AuthorizationContextHolder;
import codesquad.webserver.http.Session;
import codesquad.webserver.http.header.HeaderConstants;
import codesquad.webserver.http.header.HttpHeaders;

import java.util.Collections;
import java.util.Optional;

public class SessionMiddleWare implements MiddleWare {

    @Override
    public void applyMiddleWare(Request httpRequest, Response httpResponse) {
        HttpHeaders headers = httpRequest.getHeaders();

        try {
            Optional<String> maybeCookie = headers.getSubValueOfHeader(HeaderConstants.COOKIE, "sid");
            if(maybeCookie.isPresent()) {
                String cookie = maybeCookie.get();
                if(SessionDatabase.containsKey(cookie)) {
                    Session session = SessionDatabase.find(cookie);

                    AuthorizationContextHolder.setContext(new AuthorizationContext(session, Collections.emptyList()));
                }
            }

        }
        catch (IllegalArgumentException e) {
            // do nothing
        }
    }
}
