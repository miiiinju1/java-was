package codesquad.authorization;

import codesquad.http.Session;

import java.util.List;

public class AuthorizationContext {
    private Session session;

    // TODO roles는 Enum으로 변경하면 더 좋을 것 같습니다.
    private List<String> roles;

    public Session getSession() {
        return session;
    }

    public List<String> getRoles() {
        return roles;
    }

    public AuthorizationContext(Session session, List<String> roles) {
        this.session = session;
        this.roles = roles;
    }
}
