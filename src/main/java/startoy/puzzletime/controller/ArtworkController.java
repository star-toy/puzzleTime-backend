package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.dto.artwork.ArtworkDTO;
import startoy.puzzletime.dto.artwork.CompleteArtworksResponse;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayRequest;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayResponse;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.service.ArtworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import startoy.puzzletime.service.PuzzleService;
import startoy.puzzletime.service.TokenService;
import startoy.puzzletime.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/artworks")
@Tag(name = "ArtWorks", description = "ArtWorks API")
public class ArtworkController {

    private static final Logger logger = LoggerFactory.getLogger(ArtworkController.class);
    private final ArtworkService artworkService;
    private final UserService userService;
    private final TokenService tokenService;

    // 특정 아트웍의 기본 정보(artworkUid, artworkTitle, artworkDescription) 조회
    @GetMapping("/{artworkUid}")
    public ResponseEntity<ArtworkDTO> getArtworkByUid(@PathVariable String artworkUid) {
        logger.info("아트웍 UID '{}'에 대한 기본 정보 조회 요청을 받았습니다.", artworkUid);
        ArtworkDTO artworkDto = artworkService.getArtworkByUid(artworkUid);

        if (artworkDto == null) {
            logger.warn("아트웍 UID '{}'에 대한 정보가 없습니다.", artworkUid);
            throw new CustomException(ErrorCode.ARTWORK_NOT_FOUND);
        }

        return ResponseEntity.ok(artworkDto);
    }

    // Mypage - gallery 완성한 artwork과 reward 조회
    @Operation(summary = "complete artworks", description = "갤러리에서 완성한 작품과 보상을 조회")
    @GetMapping("/completed")
    public ResponseEntity<List<CompleteArtworksResponse>> getCompleteArtworks(
            @CookieValue(name = "token") String token) {  // 쿠키에서 앱 토큰 가져오기

        // 토큰에서 이메일 추출 후 사용자 ID 조회
        String email = tokenService.getEmailFromToken(token);
        Long userId = userService.getUserIdByEmail(email);

        List<CompleteArtworksResponse> response = artworkService.findCompleteArtworks(userId);
        return ResponseEntity.ok(response);
    }

}

