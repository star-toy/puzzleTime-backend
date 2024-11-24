package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayRequest;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayResponse;
import startoy.puzzletime.service.PuzzlePlayService;
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

    @Operation(summary = "Save Puzzle Play", description = "퍼즐 진행 상태를 저장합니다.")
    @PostMapping("/{puzzleUid}")
    public ResponseEntity<PuzzlePlayResponse> savePuzzlePlay(
            @PathVariable @Parameter(description = "퍼즐 UID", required = true) String puzzleUid,
            @RequestBody @Parameter(description = "퍼즐 진행 상태", required = true) PuzzlePlayRequest request,
            OAuth2AuthenticationToken authentication) {
        String userId = userService.getUserId(authentication);
        PuzzlePlayResponse response = puzzlePlayService.savePuzzlePlay(puzzleUid, userId, request);
        return ResponseEntity.ok(response);
    }


}
