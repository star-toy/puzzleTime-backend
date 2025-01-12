package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.dto.puzzle.GetPuzzleResponse;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.service.PuzzleService;
import startoy.puzzletime.service.TokenService;
import startoy.puzzletime.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/puzzles")
@Tag(name = "Puzzles", description = "Puzzles API")
public class PuzzleController {

    private static final Logger logger = LoggerFactory.getLogger(PuzzleController.class);
    private final PuzzleService puzzleService;
    private final UserService userService;
    private final TokenService tokenService;
    
    @Operation(summary = "Get Puzzle by UID", description = "퍼즐 UID로 퍼즐 정보를 조회합니다. 사용자가 로그인된 경우 진행 상태를 포함하여 반환합니다.")
    @GetMapping("/{puzzleUid}")
    public ResponseEntity<GetPuzzleResponse> getPuzzleById(
            @PathVariable @Parameter(description = "퍼즐 UID", required = true) String puzzleUid,
            @CookieValue(name = "token", required = false) String token) {  // token이 없어도 동작하도록 required = false

        String userId = null;
        String email = null;

        if (token != null) {
             email = tokenService.getEmailFromToken(token);

            // 이메일이 null인 경우 (만료된 토큰)
            if (email == null) {

                logger.info("Access token expired. Proceeding with refresh.");
                throw new CustomException(ErrorCode.TOKEN_EXPIRED);
            }

        }
        userId = userService.getUserIdByEmail(email).toString();
        log.info("User {} requesting puzzle {}", email, puzzleUid);


        GetPuzzleResponse puzzleResponse = puzzleService.getPuzzleByUid(puzzleUid, userId);
        return ResponseEntity.ok(puzzleResponse);
    }
}