package codesquad.http;

public class Path {

    private final String value;

    public static Path of(String path) {
        return new Path(path);
    }
    public Path(String path) {
        this.value = validatePath(path);
    }

    public String getValue() {
        return value;
    }

    private String validatePath(String path) {
        if(path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        return path;
    }
}
