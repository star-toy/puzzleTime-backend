package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startoy.puzzletime.dto.artwork.ArtworkDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.service.ArtworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}

