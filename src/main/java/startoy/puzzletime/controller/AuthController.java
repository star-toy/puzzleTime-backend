package startoy.puzzletime.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.dto.user.LoginResponseDTO;
import startoy.puzzletime.dto.user.OAuth2TokenRequestDTO;
import startoy.puzzletime.service.OAuth2Service;
import startoy.puzzletime.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

//로그인 페이지와 리다이렉트 URL을 처리하는 컨트롤러.
// 회원 가입 API는 별도의 POST로 존재할 수 있지만,
// 구글 OAuth2 로그인/회원가입은 GET 요청으로 진행됩니다.

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final OAuth2Service oAuth2Service;


    @Operation(summary = "Exchange Authorization Code for Access Token", description = "Authorization Code로 Access Token 요청")
    @PostMapping("/oauth2/token")
    public ResponseEntity<LoginResponseDTO> handleAuthorizationCode(@RequestBody OAuth2TokenRequestDTO request) {

        String authorizationCode = request.getCode(); // 프론트에서 받은 Authorization Code

        logger.debug("code : {}",authorizationCode );

        // OAuth2Service를 통해 Authorization Code 처리
        LoginResponseDTO response = oAuth2Service.processAuthorizationCode(authorizationCode);
        return ResponseEntity.ok(response);
    }

    // OAuth2 로그인 성공 후 사용자 정보 반환
    @Operation(summary = "OAuth2 로그인 성공 후 사용자 정보 반환", description = "아직 테스트중")
    @GetMapping("/oauth2/success")
    public ResponseEntity<LoginResponseDTO> handleOAuth2Login(OAuth2AuthenticationToken authentication,
            HttpServletResponse response) {


        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("OAuth2 인증 실패: 사용자 정보가 없습니다.");
            return ResponseEntity.status(401).body(new LoginResponseDTO("Authentication failed", null,null));
        }

        // 사용자 이메일 추출
        String email = authentication.getPrincipal().getAttribute("email");

        if (email == null) {
            logger.warn("OAuth2 인증 실패: 이메일 정보가 없습니다.");
            return ResponseEntity.status(401).body(new LoginResponseDTO("Email not found", null, null));
        }

        String name = authentication.getPrincipal().getAttribute("name"); // 사용자 이름
        String provider = authentication.getAuthorizedClientRegistrationId(); // OAuth 제공자 이름 (google, github 등)
        String providerId = authentication.getPrincipal().getAttribute("sub"); // 고유 사용자 ID

        // 사용자 생성 또는 조회
        User user = userService.findOrCreateUser(email, name, provider, providerId);

        // Google Access Token 및 사용자 정보 처리
        //LoginResponseDTO loginResponse = oAuth2Service.processAuthorizationCode(authorizationCode);

        // Access Token 처리 (예: Google Access Token이 OAuth2AuthenticationToken에 포함되어 있다면 활용 가능)
        String accessToken = authentication.getPrincipal().getAttribute("access_token");

        logger.debug("accessToken : {}",accessToken );


        // Access Token을 쿠키에 설정
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true); // JavaScript 접근 금지
        cookie.setSecure(true);  // HTTPS에서만 전송
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 1일 동안 유효
        response.addCookie(cookie);

        logger.info("OAuth2 로그인 성공: 사용자 '{}'", email);

        // 성공 메시지와 사용자 정보 반환
        return ResponseEntity.ok(new LoginResponseDTO("Login successful", user, accessToken));
    }

    /**
     * 로그아웃 처리
     */
    @Operation(summary = "로그아웃", description = "아직 테스트중")
    @GetMapping("/logout" )
    public ResponseEntity<String> handleLogout(HttpServletResponse response) {
        // 쿠키에서 토큰 제거
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);

        logger.info("로그아웃 성공: 토큰 쿠키 제거 완료");
        return ResponseEntity.ok("Logged out successfully");
    }


////////////////// code 값을 얻기 위한 테스트

   /* @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<String> handleAuthorizationCode(@RequestParam("code") String code) {
        // Authorization Code 로그 출력
        logger.info("Received Authorization Code: {}", code);

        // 클라이언트에게도 Authorization Code 반환
        return ResponseEntity.ok("Authorization Code: " + code);
    }*/

    ////////////////// code 값을 얻기 위한 테스트
}
