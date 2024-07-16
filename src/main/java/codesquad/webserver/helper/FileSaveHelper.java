package codesquad.webserver.helper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileSaveHelper {

    private static final String UPLOAD_DIR = "uploads";

    /**
     * UUID로 파일명을 변경하여 애플리케이션의 실행 경로에 저장하고 파일 경로를 반환하는 메서드.
     *
     * @param fileContent 파일의 바이트 배열
     * @param originalFilename 원본 파일명
     * @return 저장된 파일의 경로
     * @throws IOException 파일 저장 중 발생한 예외
     */
    public static String saveFile(byte[] fileContent, String originalFilename) throws IOException {
        // 저장 디렉토리 설정
        Path uploadDirPath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIR);
        if (!Files.exists(uploadDirPath)) {
            Files.createDirectories(uploadDirPath);
        }

        // UUID 파일명 생성
        String extension = getFileExtension(originalFilename);
        String uuidFilename = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);
        Path filePath = uploadDirPath.resolve(uuidFilename);

        // 파일 저장
        Files.write(filePath, fileContent);

        return filePath.toString();
    }

    /**
     * 파일 확장자를 반환하는 메서드.
     *
     * @param filename 파일명
     * @return 파일 확장자
     */
    private static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    private FileSaveHelper() {
    }
}
