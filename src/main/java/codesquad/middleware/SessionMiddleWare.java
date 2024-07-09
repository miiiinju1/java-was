package codesquad.middleware;

import codesquad.authorization.AuthorizationContext;
import codesquad.authorization.AuthorizationContextHolder;
import codesquad.database.SessionDatabase;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.Session;
import codesquad.http.header.HeaderConstants;
import codesquad.http.header.HttpHeaders;

import java.util.Collections;
import java.util.Optional;

public class SessionMiddleWare implements MiddleWare {

    @Override
    public void applyMiddleWare(HttpRequest request, HttpResponse response) {
        HttpHeaders headers = request.getHeaders();

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
