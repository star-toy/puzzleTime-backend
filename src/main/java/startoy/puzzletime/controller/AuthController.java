package startoy.puzzletime.controller;


import startoy.puzzletime.domain.User;
import startoy.puzzletime.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//로그인 페이지와 리다이렉트 URL을 처리하는 컨트롤러.
// 회원 가입 API는 별도의 POST로 존재할 수 있지만,
// 구글 OAuth2 로그인/회원가입은 GET 요청으로 진행됩니다.
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // OAuth2 로그인 성공 후 사용자 정보 반환
    @GetMapping("/api/auth/oauth2/success")
    public ResponseEntity<User> getUserInfo(OAuth2AuthenticationToken authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(null);  // 인증되지 않았을 때 401 응답
        }

        // 인증된 사용자 정보 가져오기
        String email = authentication.getPrincipal().getAttribute("email");
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body(null);  // 사용자가 없을 때 404 응답
        }

        return ResponseEntity.ok(user);  // 인증된 사용자 정보 반환
    }

    // 로그아웃 처리 (선택 사항)
    @GetMapping("/api/auth/logout")
    public ResponseEntity<String> logout() {
        // 여기서 로그아웃 로직을 구현
        return ResponseEntity.ok("Logged out successfully");
    }
}
