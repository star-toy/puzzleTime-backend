package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.dto.theme.ThemeWithArtworksAndPuzzlesResponseDTO;
import startoy.puzzletime.dto.theme.ThemeWithArtworksResponseDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.service.ThemeService;
import startoy.puzzletime.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/themes")
@Tag(name = "Themes", description = "Themes API")
public class ThemeController {

    private final ThemeService themeService;
    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(ThemeController.class);

     // 모든 테마와 해당 테마의 아트웍 리스트를 조회하는 API
    @GetMapping
    public ResponseEntity<List<ThemeWithArtworksResponseDTO>> getAllThemesWithArtworks() {

        logger.info("모든 테마와 아트웍 목록 조회 요청이 들어왔습니다.");
        List<ThemeWithArtworksResponseDTO> allThemesWithArtworks  = themeService.getAllThemesWithArtworks();

        if (allThemesWithArtworks  == null|| allThemesWithArtworks.isEmpty()) {
            logger.warn("조회된 테마나 아트웍이 없습니다.");
            throw new CustomException(ErrorCode.PUZZLE_NOT_FOUND);
        }

        return ResponseEntity.ok(allThemesWithArtworks );
    }


    @Operation(summary = "테마에 관련된 artwork 및 puzzle 정보", description = "테마UID 값에 따른 artwork 및 puzzle 정보 조회")
    @GetMapping("/{themeUid}/artworks")
    public ResponseEntity<ThemeWithArtworksAndPuzzlesResponseDTO> getThemeWithArtworksAndPuzzles(
            @PathVariable String themeUid,
            @CookieValue(name = "token" , required = false) String token) {

        String userEmail = null; // 기본 이메일 값은 null로 설정 (비회원)
        if (token != null) {
            try {
                userEmail = tokenService.getEmailFromToken(token); // 회원의 이메일 추출
            } catch (Exception e) {
                logger.warn("토큰 디코딩 실패: {}", e.getMessage());
            }
        }

        logger.info("계정 '{}'의 테마 UID '{}' 관련 아트웍 및 퍼즐 정보 조회 요청을 받았습니다.",
                userEmail != null ? userEmail : "비회원", themeUid);

        ThemeWithArtworksAndPuzzlesResponseDTO response = themeService.getThemeWithArtworksAndPuzzlesByUid(themeUid, userEmail);
        return ResponseEntity.ok(response);
    }
}