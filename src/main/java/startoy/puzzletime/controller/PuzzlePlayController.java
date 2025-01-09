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
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
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

        String userEmail = null; // 기본 이메일 값은 null로 설정 (비회원)

        if (token != null && !token.isEmpty()) {
            try {
                // 토큰에서 이메일 추출
                userEmail = tokenService.getEmailFromToken(token);

                // 만료된 토큰 처리
                if (userEmail == null) {
                    throw new CustomException(ErrorCode.TOKEN_EXPIRED);
                }

            } catch (CustomException e) {
                // 만료된 토큰인 경우 로그 기록
                if (e.getErrorCode() == ErrorCode.TOKEN_EXPIRED) {
                    logger.warn("만료된 토큰으로 요청되었습니다.");
                } else {
                    throw e; // 다른 예외는 그대로 던짐
                }
            }
        }

        // 회원/비회원 처리
        if (userEmail == null) {
            logger.info("No valid token found, treating request as guest user.");
            PlayingPuzzlesResponseDTO response = puzzlePlayService.getPlayingPuzzlesForGuest();
            return ResponseEntity.ok(response);
        } else {
            logger.debug("User email extracted from token: {}", userEmail);
            PlayingPuzzlesResponseDTO response = puzzlePlayService.getPlayingPuzzles(userEmail);
            return ResponseEntity.ok(response);
        }
    }
}
