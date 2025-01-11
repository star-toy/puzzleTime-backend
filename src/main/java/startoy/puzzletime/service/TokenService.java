package startoy.puzzletime.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.repository.UserRepository;
import startoy.puzzletime.util.JwtTokenProvider;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 새로운 앱 액세스 토큰 생성
    public String createAppAccessToken(User user) {
        String appAccessToken = jwtTokenProvider.createToken(user.getEmail(), user.getUserName());
        LocalDateTime expiredAt = jwtTokenProvider.getExpiredAtFromToken(appAccessToken); // 만료 시간 추출

        user.setAppAccessToken(appAccessToken);
        user.setAppTokenExpiresAt(expiredAt);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        logger.info("Created new app access token for user: {}", user.getEmail());

        return appAccessToken;
    }

    // 앱 액세스 토큰 갱신
    public String refreshAppAccessToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 토큰 만료 여부 확인(토큰이 아직 유효하면(isAppTokenExpired(user)가 false) 기존 토큰을 반환.)
        if (!isAppTokenExpired(user)) {
            logger.info("Token is still valid for user: {}", email);
            return user.getAppAccessToken(); // 기존 토큰 반환
        }

        logger.info("Token expired for user: {}. Creating new token.", email);

        // 새로운 토큰 생성 및 저장
        String newToken = createAppAccessToken(user);

        // 사용자 객체의 만료일 갱신 후 확인
        if (user.getAppTokenExpiresAt() != null && user.getAppTokenExpiresAt().isAfter(LocalDateTime.now())) {
            logger.info("New token successfully created for user: {}", email);
        } else {
            logger.warn("Token expiration date not updated correctly for user: {}", email);
        }

        return newToken;
    }

    // 토큰 만료 여부 확인
    public boolean isAppTokenExpired(User user) {
        if (user.getAppTokenExpiresAt() == null) {
            logger.warn("AppTokenExpiresAt is null for user: {}", user.getEmail());
            return true; // 만료된 것으로 간주
        }

        return user.getAppTokenExpiresAt().isBefore(LocalDateTime.now());
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        if (token == null || token.isEmpty()) {  // 비회원으로 처리
            log.info("Token is missing or empty. Treating as unauthenticated request.");
            return null;
        }
        try {
            // 토큰에서 이메일 추출
            String email = jwtTokenProvider.getEmailFromToken(token);
            logger.info("Extracted email from token: {}", email);

            // 유효성 검사
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (isAppTokenExpired(user)) {
                log.warn("Token expired for user: {}", email);

                // 만료된 토큰일 경우 이메일 반환 (갱신 로직에서 처리)
                throw new CustomException(ErrorCode.TOKEN_EXPIRED); // TOKEN_EXPIRED 사용
            }

            return email;

        }catch (ExpiredJwtException e) {
            // 만료된 토큰에서 이메일 추출
            String expiredEmail = e.getClaims().getSubject();
            logger.warn("Expired token used. Email extracted: {}", expiredEmail);
            throw new CustomException(ErrorCode.TOKEN_EXPIRED); // 명확히 TOKEN_EXPIRED 처리

        } catch (CustomException e) {
            logger.error("CustomException occurred: {}", e.getErrorCode());
            throw e; // 기존 CustomException은 그대로 던짐

        } catch (SignatureException e) {
            // 예상치 못한 오류 처리
            logger.error("Unexpected error while processing token: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.TOKEN_REISSUE_REQUIRED);

        }
    }

}
