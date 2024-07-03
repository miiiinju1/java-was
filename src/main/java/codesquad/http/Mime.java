package codesquad.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public enum Mime {
    // Text Types
    TEXT_HTML("html", "text/html", StandardCharsets.UTF_8),
    TEXT_CSS("css", "text/css", StandardCharsets.UTF_8),
    TEXT_PLAIN("txt", "text/plain", StandardCharsets.UTF_8),
    TEXT_XML("xml", "text/xml", StandardCharsets.UTF_8),
    TEXT_CSV("csv", "text/csv", StandardCharsets.UTF_8),

    // Image Types
    IMAGE_ICO("ico", "image/x-icon", null),
    IMAGE_PNG("png", "image/png", null),
    IMAGE_JPEG("jpeg", "image/jpeg", null),
    IMAGE_BMP("bmp", "image/bmp", null),
    IMAGE_SVG("svg", "image/svg+xml", StandardCharsets.UTF_8),
    IMAGE_JPG("jpg", "image/jpg", null),
    IMAGE_GIF("gif", "image/gif", null),
    IMAGE_TIFF("tiff", "image/tiff", null),
    IMAGE_WEBP("webp", "image/webp", null),

    // Application Types
    APPLICATION_JSON("json", "application/json", StandardCharsets.UTF_8),
    APPLICATION_XML("xml", "application/xml", StandardCharsets.UTF_8),
    APPLICATION_JAVASCRIPT("js", "application/javascript", StandardCharsets.UTF_8),
    APPLICATION_PDF("pdf", "application/pdf", null),
    APPLICATION_ZIP("zip", "application/zip", null),
    APPLICATION_GZIP("gz", "application/gzip", null),
    APPLICATION_RAR("rar", "application/vnd.rar", null),
    APPLICATION_TAR("tar", "application/x-tar", null),
    APPLICATION_WWW_FORM_URLENCODED("urlencoded", "application/x-www-form-urlencoded", StandardCharsets.UTF_8),
    APPLICATION_OCTET_STREAM("bin", "application/octet-stream", null);

    private final String extension;
    private final String type;
    private final Charset charset;

    private static final Map<String, Mime> mimeTypes = new HashMap<>();
    private static final Map<String, Mime> extensionTypes = new HashMap<>();

    static {
        for (Mime mime : values()) {
            mimeTypes.put(mime.getType(), mime);
            extensionTypes.put(mime.extension, mime);
        }
    }

    Mime(String extension, String type, Charset charset) {
        this.extension = extension;
        this.type = type;
        this.charset = charset;
    }

    public static boolean isSupportType(String type) {
        return mimeTypes.containsKey(type);
    }

    public static Mime ofType(String type) {
        if(!mimeTypes.containsKey(type)) {
            return TEXT_HTML;
        }
        return mimeTypes.get(type);
    }

    public static Mime ofFilePath(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("확장자는 cannot be null");
        }

        String extension = filePath.substring(filePath.lastIndexOf("."));

        if (!extensionTypes.containsKey(extension)) {
            return TEXT_HTML;
        }
        return extensionTypes.get(extension);
    }

    public String getType() {
        return type;
    }
}
