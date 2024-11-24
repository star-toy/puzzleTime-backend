package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.domain.UserRole;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.repository.UserRepository;
import org.springframework.security.oauth2.jwt.*;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

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

    // Access Token으로 이메일 가져오기 (Google Userinfo API 호출)
    public String getEmailFromAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Google Userinfo API 호출
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            // 응답에서 이메일 추출
            Map<String, Object> userInfo = response.getBody();
            if (userInfo != null && userInfo.containsKey("email")) {
                String email = (String) userInfo.get("email");
                logger.debug("Extracted email from Userinfo API: {}", email); // 디버깅용 로그
                return email;
            } else {
                throw new RuntimeException("Userinfo API response does not contain email");
            }
        } catch (Exception e) {
            logger.error("Failed to retrieve email from Access Token: {}", e.getMessage());
            return null; // 이메일을 가져오지 못한 경우
        }
    }

    // 이메일로 사용자 ID 조회
    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId) // user_id 반환
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
