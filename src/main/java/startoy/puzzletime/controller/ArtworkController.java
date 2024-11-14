package startoy.puzzletime.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startoy.puzzletime.dto.ArtworkDto;
import startoy.puzzletime.dto.puzzle.PuzzleResponseDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.service.ArtworkService;
import startoy.puzzletime.dto.puzzle.ArtworkWithPuzzlesResponseDTO;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/artworks")
@Tag(name = "ArtWorks", description = "ArtWorks API")
public class ArtworkController {

    private final ArtworkService artworkService;

    // 특정 아트웍의 기본 정보(예: 제목, 설명)를 조회
    @GetMapping("/{artworkUid}")
    public ResponseEntity<ArtworkDto> getArtworkByUid(@PathVariable String artworkUid) {
        // artworkService에서 artworkUid로 ArtworkDto를 조회하며, 없을 경우 예외 발생
        ArtworkDto artworkDto = artworkService.getArtworkByUid(artworkUid);

        return ResponseEntity.ok(artworkDto);
    }

// 특정 아트웍의 기본 정보와 퍼즐 목록을 함께 조회
    @GetMapping("/{artworkUid}/puzzles")
    public ResponseEntity<ArtworkWithPuzzlesResponseDTO> getArtworkWithPuzzles(@PathVariable String artworkUid) {
        ArtworkWithPuzzlesResponseDTO responseDto = artworkService.getArtworkWithPuzzlesByUid(artworkUid);
        return ResponseEntity.ok(responseDto);
    }
}

