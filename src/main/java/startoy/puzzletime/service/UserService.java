package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.domain.UserRole;
import startoy.puzzletime.dto.user.UserWithStatusDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final TokenService tokenService;


    // 사용자 조회 또는 생성 로직
    public UserWithStatusDTO findOrCreateUser(String email, String name, String provider, String providerId, String refreshToken) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = updateExistingUser(existingUser.get(), refreshToken);

            logger.info("Existing user logged in: {}", email);

            return new UserWithStatusDTO(user, false);
        } else {
            User newUser = createNewUser(email, name, provider, providerId, refreshToken);

            logger.info("New user registered: {}", email);
            return new UserWithStatusDTO(newUser, true);
        }
    }

    // 기존 사용자 정보 업데이트
    private User updateExistingUser(User user, String refreshToken) {

        // refresh_token이 제공된 경우에만 업데이트
        if (refreshToken != null && !refreshToken.isEmpty()) {
            user.setRefreshToken(refreshToken);
        }

        user.setUpdatedAt(LocalDateTime.now());
        String appAccessToken = tokenService.createAppAccessToken(user);
        user.setAppAccessToken(appAccessToken);
        return userRepository.save(user);
    }

    // 새 사용자 생성
    public User createNewUser(String email, String name, String provider, String providerId, String refreshToken) {
        User newUser = User.builder()
                .email(email)
                .userName(name != null ? name : "New User")
                .provider(provider)
                .providerId(providerId)
                .refreshToken(refreshToken)
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        // 토큰 생성 및 설정
        String appAccessToken = tokenService.createAppAccessToken(newUser);
        newUser.setAppAccessToken(appAccessToken);

        return userRepository.save(newUser);
    }

    // 이메일로 사용자 ID 조회
    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 이메일로 사용자 ID 조회 : 게스트도 사용할 수 있는 로직에 활용
    public Optional<Long> findUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId); // 데이터가 없을 경우 빈 Optional 반환
    }

    // 이메일로 사용자 조회
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // ID로 사용자 조회
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}