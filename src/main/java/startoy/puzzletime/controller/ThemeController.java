package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startoy.puzzletime.dto.theme.ThemeWithArtworksResponseDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/themes")
@Tag(name = "Themes", description = "Themes API")
public class ThemeController {

    private final ThemeService themeService;
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

     // 특정 테마 UID에 해당하는 테마와 아트웍 리스트를 조회하는 API
    @GetMapping("/{themeUid}")
    public ResponseEntity<ThemeWithArtworksResponseDTO> getThemeWithArtworksByUid(@PathVariable String themeUid) {

        logger.info("테마 UID '{}'에 해당하는 테마와 아트웍 조회 요청이 들어왔습니다.", themeUid);
        ThemeWithArtworksResponseDTO themeWithArtworksByUid = themeService.getThemeWithArtworksByUid(themeUid);

        if (themeWithArtworksByUid == null) {
            logger.warn("테마 UID '{}'에 해당하는 데이터가 없습니다.", themeUid);
            throw new CustomException(ErrorCode.THEME_NOT_FOUND);
        }

        return ResponseEntity.ok(themeWithArtworksByUid);
    }
}