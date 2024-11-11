package startoy.puzzletime.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startoy.puzzletime.dto.ArtworkDto;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.service.ArtworkService;

@RestController
@RequestMapping("api/artworks")
public class ArtworkController {

    private ArtworkService artworkService;

    // Uid 값으로
    @GetMapping("/{artworkUid}")
    public ResponseEntity<ArtworkDto> getArtworkByUid(@PathVariable String artworkUid) {
        ArtworkDto artworkDto = artworkService.getArtworkByUid(artworkUid);

        // 만약 artworkDto가 null이면 NotFoundException 예외 발생
        if (artworkDto == null) {
            throw new CustomException(ErrorCode.ARTWORK_NOT_FOUND);
        }

        return ResponseEntity.ok(artworkDto);
    }

}

