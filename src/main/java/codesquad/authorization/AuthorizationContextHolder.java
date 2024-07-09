package codesquad.authorization;

public class AuthorizationContextHolder {

    private static final ThreadLocal<AuthorizationContext> contextHolder = new ThreadLocal<>();

    public static void setContext(AuthorizationContext context) {
        contextHolder.set(context);
    }

    public static boolean isAuthorized() {
        return contextHolder.get() != null;
    }

    public static AuthorizationContext getContext() {
        return contextHolder.get();
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    private AuthorizationContextHolder() {
    }
}
