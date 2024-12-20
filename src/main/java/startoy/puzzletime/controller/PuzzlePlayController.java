package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/puzzlePlays")
@Tag(name = "PuzzlePlays", description = "PuzzlePlays API")
public class PuzzlePlayController {

    private static final Logger logger = LoggerFactory.getLogger(PuzzlePlayController.class);
    private final PuzzlePlayService puzzlePlayService;
    private final UserService userService;
    private final TokenService tokenService;

    @Operation(summary = "Save Puzzle Play", description = "퍼즐 진행 상태를 저장합니다.")
    @PostMapping("/{puzzlePlayUID}")
    public ResponseEntity<PuzzlePlayResponse> savePuzzlePlay(
            @PathVariable @Parameter(description = "퍼즐 play UID", required = true) String puzzlePlayUID,
            @RequestBody @Parameter(description = "퍼즐 진행 상태", required = true) PuzzlePlayRequest request,
            @CookieValue(name = "token") String token) {  // 쿠키에서 앱 토큰 가져오기

        // 토큰에서 이메일 추출 후 사용자 ID 조회
        String email = tokenService.getEmailFromToken(token);
        Long userId = userService.getUserIdByEmail(email);

        PuzzlePlayResponse response = puzzlePlayService.savePuzzlePlay(puzzlePlayUID, userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "플레이 중인 퍼즐 목록 조회", description = "현재 사용자의 진행 중인 퍼즐 목록을 조회합니다.")
    @GetMapping("/playing")
    public ResponseEntity<PlayingPuzzlesResponseDTO> getPlayingPuzzles(@CookieValue(name = "token") String token) {

        // 토큰에서 이메일 추출 후 사용자 ID 조회
        String email = tokenService.getEmailFromToken(token);

        logger.debug("userEmail : {}", email);

        // 진행 중인 퍼즐 데이터 가져오기
        PlayingPuzzlesResponseDTO response = puzzlePlayService.getPlayingPuzzles(email);

        return ResponseEntity.ok(response);
    }
}
