package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import startoy.puzzletime.repository.FileStorageRepository;
import startoy.puzzletime.domain.FileStorage;
import startoy.puzzletime.dto.FileStorageDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorageRepository fileStorageRepository;
    private final S3Service s3Service;


    // 파일 S3 업로드
    @Transactional
    public FileStorageDTO saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        try {
            // 새로운 UUID 생성
            String fileUid = UUID.randomUUID().toString();

            // 원본 파일명과 확장자 추출
            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null || !originalFileName.contains(".")) {
                throw new FileValidationException("Invalid file name: The file name is either null or missing a file extension.");
            }

            // 확장자 추출
            String fileExtension = getFileExtension(originalFileName);

            // 파일 UID를 포함한 새 파일명 생성
            String fileName = String.format("%s_%s.%s",
                    originalFileName.substring(0, originalFileName.lastIndexOf('.')),
                    fileUid,
                    fileExtension);

            // S3에 파일 업로드
            String fileUrl = s3Service.uploadFile(file, fileName);
            System.out.println("fileUrl : " + fileUrl);

            // FileStorage 엔티티 생성 및 데이터베이스에 저장
            FileStorage fileStorage = FileStorage.builder()
                    .fileUid(fileUid)
                    .fileName(fileName)
                    .isDeleted(false)
                    .createdAt(LocalDateTime.now())
                    .createdBy("system") // 필요에 따라 설정
                    .build();

            //엔티티를 데이터베이스에 저장합니다.
                    fileStorageRepository.save(fileStorage);

            // FileStorageDTO 생성 및 반환
            return  FileStorageDTO.builder()
                    .fileUid(fileUid)
                    .fileName(fileName)
                    .build();

        } catch (FileValidationException e) {
            throw e;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new FileStorageException("Failed to store file on S3", e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new FileStorageException("An unexpected error occurred while processing the file", e);
        }
    }


    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    // Custom Exception for file validation issues
    public static class FileValidationException extends RuntimeException {
        public FileValidationException(String message) {
            super(message);
        }
    }

    // Custom Exception for file storage issues
    public static class FileStorageException extends RuntimeException {
        public FileStorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }


    // fileUid를 이용하여 파일 정보를 조회하는 메서드
    @Transactional(readOnly = true)
    public FileStorage getFileByUid(String fileUid) {
        return fileStorageRepository.findByFileUid(fileUid)
                .orElseThrow(() -> new IllegalArgumentException("File not found with UID: " + fileUid));
    }

    // FileStorage 엔티티를 FileStorageDTO로 변환하는 메서드
    @Transactional(readOnly = true)
    public FileStorageDTO getFileDtoByUid(String fileUid) {
        FileStorage fileStorage = getFileByUid(fileUid);
        return convertToDto(fileStorage);
    }

    // FileStorage 엔티티를 FileStorageDTO로 변환
    private FileStorageDTO convertToDto(FileStorage fileStorage) {
        return FileStorageDTO.builder()
                .fileUid(fileStorage.getFileUid())
                .fileName(fileStorage.getFileName())
                .build();
    }


    // S3에 파일을 업로드하고 파일의 UID와 이름을 반환하는 메서드
    public String uploadFileToS3(MultipartFile file, String fileUid) throws IOException {
        // 파일 이름을 고유하게 생성
        String fileName = fileUid + "_" + file.getOriginalFilename();

        // S3에 파일 업로드
        String fileUrl = s3Service.uploadFile(file, fileName); // S3에 업로드

        System.out.println("fileUrl : " + fileUrl);
        return fileUrl; // 업로드된 파일 URL 반환
    }

}