package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startoy.puzzletime.dto.puzzle.GetPuzzleResponse;
import startoy.puzzletime.service.PuzzleService;
import startoy.puzzletime.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/puzzles")
@Tag(name = "Puzzles", description = "Puzzles API")
public class PuzzleController {

    private final PuzzleService puzzleService;
    private final UserService userService;
    
    @Operation(summary = "Get Puzzle by UID", description = "퍼즐 UID로 퍼즐 정보를 조회합니다. 사용자가 로그인된 경우 진행 상태를 포함하여 반환합니다.")
    @GetMapping("/{puzzleUid}")
    public ResponseEntity<GetPuzzleResponse> getPuzzleById(
            @PathVariable @Parameter(description = "퍼즐 UID", required = true) String puzzleUid,
            OAuth2AuthenticationToken authentication) {
        String userId = userService.getUserId(authentication);
        GetPuzzleResponse puzzleResponse = puzzleService.getPuzzleByUid(puzzleUid, userId);
        return ResponseEntity.ok(puzzleResponse);
    }
}
