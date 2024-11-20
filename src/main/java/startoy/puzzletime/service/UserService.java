package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import startoy.puzzletime.controller.ArtworkController;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.domain.UserRole;
import startoy.puzzletime.repository.UserRepository;
import org.springframework.security.oauth2.jwt.*;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public String getUserId(OAuth2AuthenticationToken authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getPrincipal().getAttribute("sub");
        } else {
            return "";
        }
    }

    // 사용자 조회 또는 생성 로직
    public User findOrCreateUser(String email, String name, String provider, String providerId) {
        return userRepository.findByEmail(email).orElseGet(() -> createNewUser(email, name, provider, providerId));
    }


    public User createNewUser(String email, String name, String provider, String providerId) {
        User newUser = User.builder()
                .email(email)
                .userName(name != null ? name : "New User")
                .provider(provider)
                .providerId(providerId)
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(newUser);
    }

    public String getEmailFromAccessToken(String token) {
        try {
            // 디코더 생성
            JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation("https://accounts.google.com"); // Google 인증 서버
            Jwt decodedToken = jwtDecoder.decode(token);

            // 이메일 클레임 추출
            return decodedToken.getClaimAsString("email");
        } catch (Exception e) {
            logger.error("토큰 파싱 중 오류 발생: {}", e.getMessage());
            return null; // 유효하지 않은 토큰
        }
    }
}
