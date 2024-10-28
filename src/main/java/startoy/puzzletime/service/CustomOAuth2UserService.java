package startoy.puzzletime.service;

//OAuth2 로그인 성공 후 사용자 정보를 처리하는 서비스.

import startoy.puzzletime.domain.User;
import startoy.puzzletime.domain.UserRole;
import startoy.puzzletime.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2 사용자 정보
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // 이미 사용자 정보가 있는지 확인
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            // 없으면 새 사용자 생성
            return User.builder()
                    .userUid(UUID.randomUUID().toString())
                    .userName(name)
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .role(UserRole.USER)  // 기본적으로 USER 역할을 부여
                    .build();
        });

        // 사용자 정보가 새로 추가되었으면 저장
        if (user.getId() == null) {
            userRepository.save(user);
        }

        return oAuth2User;
    }
}