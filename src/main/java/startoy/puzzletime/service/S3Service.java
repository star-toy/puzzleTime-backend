package startoy.puzzletime.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        try {


            String bucket = System.getenv("AWS_S3_BUCKET");
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);

            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (AmazonServiceException e) {
            throw new S3UploadException("Error occurred while uploading file to S3", e);
        } catch (SdkClientException e) {
            throw new S3UploadException("Error occurred while communicating with S3", e);
        } catch (IOException e) {
            throw new S3UploadException("Error occurred while processing the file", e);
        }
    }

    // Custom Exception for S3 upload issues
    public static class S3UploadException extends RuntimeException {
        public S3UploadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
