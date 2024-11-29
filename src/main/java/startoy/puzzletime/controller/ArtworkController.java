package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.dto.artwork.ArtworkDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.service.ArtworkService;
import startoy.puzzletime.dto.puzzle.ArtworkWithPuzzlesResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import startoy.puzzletime.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/artworks")
@Tag(name = "ArtWorks", description = "ArtWorks API")
public class ArtworkController {

    private static final Logger logger = LoggerFactory.getLogger(ArtworkController.class);
    private final ArtworkService artworkService;

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

    // 특정 아트웍의 기본 정보와 퍼즐 목록을 함께 조회
    @GetMapping("/{artworkUid}/puzzles")
    public ResponseEntity<ArtworkWithPuzzlesResponseDTO> getArtworkWithPuzzles(
            @PathVariable String artworkUid,
            @RequestParam(value = "email", required = false) String userEmail) { // 프론트엔드에서 전달받은 사용자 이메일

        logger.info("아트웍 UID '{}'의 기본 정보 및 퍼즐 목록 조회 요청을 받았습니다.", artworkUid);
        ArtworkWithPuzzlesResponseDTO responseDto = artworkService.getArtworkWithPuzzlesByUidAndUser(artworkUid, userEmail);

        if (responseDto == null) {
            logger.warn("아트웍 UID '{}'에 대한 퍼즐 목록이 없습니다.", artworkUid);
            throw new CustomException(ErrorCode.PUZZLE_NOT_FOUND);
        }

        // 응답 DTO에 email 설정
        responseDto.setEmail(userEmail); // 비회원일 경우 null이 설정됨

        return ResponseEntity.ok(responseDto);
    }
}

