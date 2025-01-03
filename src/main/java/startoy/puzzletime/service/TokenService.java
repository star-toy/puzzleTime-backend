package startoy.puzzletime.service;

import io.jsonwebtoken.ExpiredJwtException;
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
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

        user.setAppAccessToken(appAccessToken);
        user.setAppTokenExpiresAt(expiresAt);
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
            return user.getAppAccessToken();
        }

        logger.info("Token expired for user: {}. Creating new token.", email);
        return createAppAccessToken(user);
    }

    // 토큰 만료 여부 확인
    public boolean isAppTokenExpired(User user) {
        return user.getAppTokenExpiresAt() != null &&
                user.getAppTokenExpiresAt().isBefore(LocalDateTime.now());
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
                return email;
            }

            return email;

        }catch (ExpiredJwtException e) {
            // 만료된 토큰에서 이메일 추출
            String expiredEmail = e.getClaims().getSubject();
            log.warn("Expired token used, email extracted: {}", expiredEmail);
            return expiredEmail; // 갱신 로직으로 이어짐
        } catch (Exception e) {
            log.error("Failed to extract email from token: {}", e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    // 토큰의 유효성만 별도로 확인하는 메서드
    /*public boolean isTokenValid(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            String email = jwtTokenProvider.getEmailFromToken(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            return !isAppTokenExpired(user); // 만료되지 않았으면 true
        } catch (Exception e) {
            log.warn("Token is invalid or expired: {}", e.getMessage());
            return false;
        }
    }*/
}
