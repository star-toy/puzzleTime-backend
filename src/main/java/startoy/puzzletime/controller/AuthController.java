package startoy.puzzletime.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.dto.user.LoginRequestDTO;
import startoy.puzzletime.dto.user.LoginResponseDTO;
import startoy.puzzletime.dto.user.UserWithStatusDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.service.TokenService;
import startoy.puzzletime.service.UserService;
import org.springframework.http.ResponseEntity;
import startoy.puzzletime.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

//로그인 페이지와 리다이렉트 URL을 처리하는 컨트롤러.
// 회원 가입 API는 별도의 POST로 존재할 수 있지만,
// 구글 OAuth2 로그인/회원가입은 GET 요청으로 진행됩니다.

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
@SecurityRequirement(name = "cookieAuth") // 쿠키 인증 요구사항 추가
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final TokenService tokenService;

    @Operation(summary = "로그인/회원가입", description = "프론트엔드에서 사용자 정보를 전달받아 처리")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> handleLogin(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        logger.info("Received login request for email: {}", request.getEmail());

        // 사용자 생성 또는 조회 (앱 액세스 토큰은 서비스에서 생성)
        UserWithStatusDTO userWithStatus = userService.findOrCreateUser(
                request.getEmail(),
                request.getName(),
                request.getProvider(),
                request.getProviderId(),
                request.getRefreshToken()
        );

        // 신규 사용자 여부 확인 (생성 시간과 수정 시간이 같으면 신규 사용자)
        User user = userWithStatus.getUser();
        String message = userWithStatus.isNewUser() ? "Registration successful" : "Login successful";

        logger.info("{} for user: {}", message, user.getEmail());

        // 쿠키에 앱 액세스 토큰 설정
        Cookie cookie = new Cookie("token", user.getAppAccessToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);

        return ResponseEntity.ok(LoginResponseDTO.builder()
                .message(message)
                .user(user)
                .appAccessToken(user.getAppAccessToken())
                .isNewUser(userWithStatus.isNewUser())
                .build());
    }

    @Operation(summary = "토큰 갱신", description = "만료된 앱 액세스 토큰 갱신")
    @PostMapping("/token/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(
            @CookieValue(name = "token", required = false) String token,
            HttpServletResponse response) {

        logger.info("Token refresh requested");

        // 토큰 갱신
        String email = tokenService.getEmailFromToken(token);

        // 이메일이 없으면 유효하지 않은 토큰
        if (email == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰 갱신
        String newToken = tokenService.refreshAppAccessToken(email);

        // 새로운 토큰으로 쿠키 업데이트
        Cookie cookie = new Cookie("token", newToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);

        User user = userService.getUserByEmail(email);
        logger.info("Token refreshed successfully for user: {}", email);


        return ResponseEntity.ok(LoginResponseDTO.builder()
                .message("Token refreshed")
                .user(user)
                .appAccessToken(newToken)
                .isNewUser(false)  // 토큰 갱신은 항상 기존 사용자
                .build());
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리")
    @GetMapping("/logout")
    public ResponseEntity<String> handleLogout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        logger.info("로그아웃 성공: 토큰 쿠키 제거 완료");
        return ResponseEntity.ok("Logged out successfully");
    }
}