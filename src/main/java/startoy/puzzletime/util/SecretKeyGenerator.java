package startoy.puzzletime.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecretKeyGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SecretKeyGenerator.class);

    private static final SecureRandom secureRandom = new SecureRandom();

    public String generateSecretKey() {
        // 현재 시간 정보 포맷팅 (예: 20240319143022)
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 랜덤 바이트 생성 (32바이트 = 256비트)
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        // UUID 생성
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 모든 요소 조합
        String combined = String.format("%s_%s_%s",
                timestamp,
                uuid,
                Base64.getEncoder().encodeToString(randomBytes)
        );

        logger.info("combined : {}",combined);
        logger.info("Final_AccessToken : {}",Base64.getEncoder().encodeToString(combined.getBytes()));

        // Base64로 인코딩하여 최종 키 생성
        return Base64.getEncoder().encodeToString(combined.getBytes());
    }
}