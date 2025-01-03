package startoy.puzzletime.service;

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
            return jwtTokenProvider.getEmailFromToken(token);
        } catch (Exception e) {
            log.error("Failed to extract email from token: {}", e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

}

