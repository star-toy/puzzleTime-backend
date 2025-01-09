package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.dto.puzzlePlay.PlayingPuzzlesResponseDTO;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayRequest;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayResponse;
import startoy.puzzletime.service.PuzzlePlayService;
import startoy.puzzletime.service.TokenService;
import startoy.puzzletime.service.UserService;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/puzzlePlays")
@Tag(name = "PuzzlePlays", description = "PuzzlePlays API")
public class PuzzlePlayController {

    private static final Logger logger = LoggerFactory.getLogger(PuzzlePlayController.class);
    private final PuzzlePlayService puzzlePlayService;
    private final UserService userService;
    private final TokenService tokenService;

    @Operation(summary = "회원용 퍼즐 현황 저장",
            description = "퍼즐의 진행 상태를 저장합니다. puzzlePlayData의 갯수는 정해져 있지 않습니다.")
    @PostMapping("/{puzzlePlayUID}")
    public ResponseEntity<PuzzlePlayResponse> savePuzzlePlay(
            @PathVariable @Parameter(description = "퍼즐 play UID", required = true) String puzzlePlayUID,
            @RequestBody @Parameter(description = "퍼즐 진행 상태", required = true) PuzzlePlayRequest request,
            @CookieValue(name = "token", required = true) String token) {  // 쿠키에서 앱 토큰 가져오기

        // 토큰에서 이메일 추출 후 사용자 ID 조회
        String email = tokenService.getEmailFromToken(token);
        Long userId = userService.getUserIdByEmail(email);

        PuzzlePlayResponse response = puzzlePlayService.savePuzzlePlay(puzzlePlayUID, userId, request);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "게스트용 퍼즐 현황 저장", description = "퍼즐 진행 상태를 저장합니다.")
    @PostMapping("guest/{puzzlePlayUID}")
    public ResponseEntity<PuzzlePlayResponse> savePuzzlePlayForGuest(
            @PathVariable @Parameter(description = "퍼즐 play UID", required = true) String puzzlePlayUID,
            @RequestBody @Parameter(description = "퍼즐 진행 상태", required = true) PuzzlePlayRequest request) {

        PuzzlePlayResponse response = puzzlePlayService.savePuzzlePlay(puzzlePlayUID, userService.GUEST_USER_ID, request);
        return ResponseEntity.ok(response);
    }
  
  
    @Operation(summary = "플레이 중인 퍼즐 목록 조회", description = "현재 사용자의 진행 중인 퍼즐 목록을 조회합니다.")
    @GetMapping("/playing")
    public ResponseEntity<PlayingPuzzlesResponseDTO> getPlayingPuzzles(@CookieValue(name = "token", required = false) String token) {

        // 토큰에서 이메일 추출 (비회원이면 null 반환)
        String email = tokenService.getEmailFromToken(token);

        if (email == null) {
            // 비회원 처리: 게스트 데이터 조회
            logger.info("No token found, treating request as guest user.");
            PlayingPuzzlesResponseDTO response = puzzlePlayService.getPlayingPuzzlesForGuest();
            return ResponseEntity.ok(response);
        }

        // 회원 처리
        logger.debug("User email extracted from token: {}", email);
        PlayingPuzzlesResponseDTO response = puzzlePlayService.getPlayingPuzzles(email);
        return ResponseEntity.ok(response);
    }
}
