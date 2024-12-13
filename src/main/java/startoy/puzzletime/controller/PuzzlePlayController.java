package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayRequest;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayResponse;
import startoy.puzzletime.service.PuzzlePlayService;
import startoy.puzzletime.service.TokenService;
import startoy.puzzletime.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/puzzlePlays")
@Tag(name = "PuzzlePlays", description = "PuzzlePlays API")
public class PuzzlePlayController {

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
}
