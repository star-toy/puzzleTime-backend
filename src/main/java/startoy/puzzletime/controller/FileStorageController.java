package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import startoy.puzzletime.dto.FileStorageDTO;
import startoy.puzzletime.service.FileStorageService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/files")
@Tag(name = "FileStorage", description = "FileStorage API")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    
    /*@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 업로드")
    public ResponseEntity<FileStorageDTO> uploadFile(@RequestPart("file") MultipartFile file) {

        try {
            // 파일 유효성 검사
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("File must not be null or empty");
            }

            // FileStorageService의 saveFile 메서드 호출하여 파일 S3 업로드
            FileStorageDTO fileStorageDTO = fileStorageService.saveFile(file);

            // FileStorageDTO 반환
            return ResponseEntity.ok(fileStorageDTO);

        } catch (IllegalArgumentException e) {
            // 클라이언트 오류 처리
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(null); // FileStorageDTO 대신 null 반환
        } catch (Exception e) {
            // 기타 서버 오류 처리
            Map<String, String> response = new HashMap<>();
            response.put("message", "An error occurred while uploading the file");
            return ResponseEntity.status(500).body(null); // FileStorageDTO 대신 null 반환
        }
    }


    @GetMapping("/{fileUid}")
    @Operation(summary = "fileUid를 이용하여 파일 URL을 조회")
    public ResponseEntity<FileStorageDTO> getFileByUid(@PathVariable String fileUid) {
        try {
            FileStorageDTO fileStorageDto = fileStorageService.getFileDtoByUid(fileUid);

            return ResponseEntity.ok(fileStorageDto); // 200 OK, FileStorageDTO 반환
        } catch (IllegalArgumentException e) {
            // 잘못된 UID 요청 (클라이언트 오류)
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        } catch (Exception e) {
            // 기타 서버 오류
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }
    */
}