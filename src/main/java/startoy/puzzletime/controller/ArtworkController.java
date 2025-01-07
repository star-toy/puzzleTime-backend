package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.dto.artwork.ArtworkDTO;
import startoy.puzzletime.dto.puzzle.ArtworkWithPuzzlesResponseDTO;
import startoy.puzzletime.dto.artwork.CompleteArtworksResponse;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.service.ArtworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    /*@GetMapping("/{artworkUid}")
    public ResponseEntity<ArtworkDTO> getArtworkByUid(@PathVariable String artworkUid) {
        logger.info("아트웍 UID '{}'에 대한 기본 정보 조회 요청을 받았습니다.", artworkUid);
        ArtworkDTO artworkDto = artworkService.getArtworkByUid(artworkUid);

        if (artworkDto == null) {
            logger.warn("아트웍 UID '{}'에 대한 정보가 없습니다.", artworkUid);
            throw new CustomException(ErrorCode.ARTWORK_NOT_FOUND);
        }

        return ResponseEntity.ok(artworkDto);
    }*/


    @Operation(summary = "Mypage - gallery 완성한 artwork 목록 조회", description = "갤러리에서 완성한 작품과 보상을 조회")
    @GetMapping("/completed")
    public ResponseEntity<List<CompleteArtworksResponse>> getCompleteArtworks(
            @CookieValue(name = "token", required = false) String token ) {  // 쿠키에서 앱 토큰 가져오기
        // required=true 값으로 할 경우 쿠키가 없으므로 400 에러가 발생하므로 부적절하여 false 로 진행

        // Token 값이 없는 경우 401 반환
        if (token == null || token.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰에서 이메일 추출 후 사용자 ID 조회
        String email = tokenService.getEmailFromToken(token);

        // 이메일이 null인 경우 (만료된 토큰)
        if (email == null) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        Long userId = userService.getUserIdByEmail(email);

        List<CompleteArtworksResponse> response = artworkService.findCompleteArtworks(userId);
        return ResponseEntity.ok(response);
    }

    // 특정 아트웍의 기본 정보와 퍼즐 목록을 함께 조회
    @GetMapping("/{artworkUid}/puzzles")
    public ResponseEntity<ArtworkWithPuzzlesResponseDTO> getArtworkWithPuzzles(
            @PathVariable String artworkUid,
            @CookieValue(name = "token", required = false) String token) {

        // Token 값이 없는 경우 401 반환
        if (token == null || token.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰에서 이메일 추출 후 사용자 ID 조회
        String userEmail = tokenService.getEmailFromToken(token);

        // 이메일이 null인 경우 (만료된 토큰)
        if (userEmail == null) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

            logger.info("계정 '{}'의 아트웍 UID '{}' 기본 정보 및 퍼즐 목록 조회 요청을 받았습니다.", userEmail, artworkUid);

            ArtworkWithPuzzlesResponseDTO responseDto = artworkService.getArtworkWithPuzzlesByUidAndUser(artworkUid, userEmail);

            return ResponseEntity.ok(responseDto);
        }

}


