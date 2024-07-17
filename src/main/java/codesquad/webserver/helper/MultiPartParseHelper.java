package codesquad.webserver.helper;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultiPartParseHelper {

    public static Map<String, MultiPart> parse(InputStream inputStream, String boundary) throws IOException {
        Map<String, MultiPart> parts = new HashMap<>();
        byte[] boundaryBytes = ("--" + boundary).getBytes("UTF-8");
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] requestData = outputStream.toByteArray();
        int pos = 0;

        while (pos < requestData.length) {
            int boundaryIndex = indexOf(requestData, boundaryBytes, pos);
            if (boundaryIndex == -1) break;
            int partStart = boundaryIndex + boundaryBytes.length + 2;
            int partEnd = indexOf(requestData, boundaryBytes, partStart);
            if (partEnd == -1) break;
            byte[] partData = Arrays.copyOfRange(requestData, partStart, partEnd - 2);

            MultiPart part = parsePart(partData);
            parts.put(part.getName(), part);

            pos = partEnd;
        }

        return parts;
    }

    private static int indexOf(byte[] data, byte[] target, int start) {
        for (int i = start; i <= data.length - target.length; i++) {
            boolean found = true;
            for (int j = 0; j < target.length; j++) {
                if (data[i + j] != target[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
    }

    private static MultiPart parsePart(byte[] partData) throws IOException {
        int headerEndIndex = indexOf(partData, "\r\n\r\n".getBytes("UTF-8"), 0);
        if (headerEndIndex == -1) {
            throw new IOException("Invalid part data: missing headers");
        }

        byte[] headerData = Arrays.copyOfRange(partData, 0, headerEndIndex);
        byte[] contentData = Arrays.copyOfRange(partData, headerEndIndex + 4, partData.length);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(headerData), "UTF-8"));
        String line;
        String name = null;
        String filename = null;
        String contentType = null;

        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Disposition")) {
                name = extractHeaderValue(line, "name");
                filename = extractHeaderValue(line, "filename");
            } else if (line.startsWith("Content-Type")) {
                contentType = line.split(": ")[1];
            }
        }

        return new MultiPart(name, filename, contentType, contentData);
    }

    private static String extractHeaderValue(String header, String key) {
        String[] parts = header.split("; ");
        for (String part : parts) {
            if (part.startsWith(key + "=")) {
                return part.split("=")[1].replace("\"", "");
            }
        }
        return null;
    }

    public static class MultiPart {
        private final String name;
        private final String filename;
        private final String contentType;
        private final byte[] content;

        public MultiPart(String name, String filename, String contentType, byte[] content) {
            this.name = name;
            this.filename = filename;
            this.contentType = contentType;
            this.content = content;
        }

        public boolean isFile() {
            return filename != null;
        }

        public String getName() {
            return name;
        }

        public String getFilename() {
            return filename;
        }

        public String getContentType() {
            return contentType;
        }

        public byte[] getContent() {
            return content;
        }

        public String getTextContent() {
            try {
                return new String(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private MultiPartParseHelper() {
    }
}
