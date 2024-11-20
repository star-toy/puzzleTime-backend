package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.domain.UserRole;
import startoy.puzzletime.dto.user.LoginResponseDTO;
import startoy.puzzletime.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Service.class);
    private final UserRepository userRepository;
    private final RestTemplate restTemplate; // HTTP 요청을 위한 RestTemplate

    String clientId = System.getenv("REGISTRATION_GOOGLE_CLIENT_ID");
    String clientSecret = System.getenv("REGISTRATION_GOOGLE_CLIENT_SECRET");
    String redirectUri = System.getenv("REGISTRATION_GOOGLE_REDIRECT_URI");

    public LoginResponseDTO processAuthorizationCode(String authorizationCode) {

        // 1. Google에 Access Token 요청
        Map<String, String> tokenResponse = exchangeAuthorizationCodeForToken(authorizationCode);

        String accessToken = tokenResponse.get("access_token");
        logger.debug("access_token");

        // 2. Access Token을 사용하여 사용자 정보 요청
        Map<String, Object> userInfo = getUserInfoFromGoogle(accessToken);

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String provider = "google";
        String providerId = (String) userInfo.get("sub");

        // 3. 사용자 정보 저장 또는 조회
        User user = findOrCreateUser(email, name, provider, providerId);


        System.out.println("accessToken : "+accessToken);

        // 4. 사용자 정보와 Access Token 반환
        return new LoginResponseDTO("Login successful", user, accessToken);
    }

    private Map exchangeAuthorizationCodeForToken(String authorizationCode) {

        // Google OAuth2 Token Endpoint URL
        String tokenUrl = "https://oauth2.googleapis.com/token";

        // HTTP 요청을 위한 Request Body 생성
        Map<String, String> body = Map.of(
                "code", authorizationCode,
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "grant_type", "authorization_code"
        );

        System.out.println(body);

        // HTTP POST 요청으로 Google에서 Access Token 가져오기
        //return restTemplate.postForObject(tokenUrl, body, Map.class);
        // Google 서버에 POST 요청
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(tokenUrl, body, Map.class);

            // 응답에서 Access Token 추출
            if (response != null && response.containsKey("access_token")) {
                return restTemplate.postForObject(tokenUrl, body, Map.class);
               // return (String) response.get("access_token");
            } else {
                throw new RuntimeException("Google Access Token 요청 실패: 응답이 유효하지 않습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Google Access Token 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private Map getUserInfoFromGoogle(String accessToken) {
        // Google 사용자 정보 Endpoint URL
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        // HTTP 요청 헤더에 Access Token 포함
        String authorizationHeader = "Bearer " + accessToken;

        return restTemplate.getForObject(
                userInfoUrl,
                Map.class,
                Map.of("Authorization", authorizationHeader)
        );
    }

    private User findOrCreateUser(String email, String name, String provider, String providerId) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .userName(name)
                    .provider(provider)
                    .providerId(providerId)
                    .role(UserRole.USER)
                    .createdAt(LocalDateTime.now())
                    .build();
            return userRepository.save(newUser);
        });
    }
}