package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Tag(name = "MyPage", description = "MyPage API")
public class MyPageController {

    /*private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);
    private final MyPageService myPageService;

    @Operation(summary = "플레이 중인 퍼즐 목록 조회", description = "현재 사용자의 진행 중인 퍼즐 목록을 조회합니다.")
    @GetMapping("/puzzles/playing")
    public ResponseEntity<MyPagePlayingPuzzlesResponseDTO> getPlayingPuzzles(Authentication authentication) {
        // 현재 로그인한 사용자 이메일 가져오기
        String userEmail = authentication.getName();
        log.info("userEmail : {}",userEmail);

        logger.debug("userEmail : {}",userEmail);

        // MyPageService 호출하여 데이터 가져오기
        MyPagePlayingPuzzlesResponseDTO response = myPageService.getPlayingPuzzles(userEmail);

        return ResponseEntity.ok(response);
    }*/
}
