package startoy.puzzletime.controller;

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

import java.util.List;

@RestController
@RequestMapping("api/artworks")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    // 특정 아트웍의 기본 정보(예: 제목, 설명)를 조회
    @GetMapping("/{artworkUid}")
    public ResponseEntity<ArtworkDto> getArtworkByUid(@PathVariable String artworkUid) {
        ArtworkDto artworkDto = artworkService.getArtworkByUid(artworkUid);

        // 만약 artworkDto가 null이면 NotFoundException 예외 발생
        if (artworkDto == null) {
            throw new CustomException(ErrorCode.ARTWORK_NOT_FOUND);
        }

        return ResponseEntity.ok(artworkDto);
    }

    // 특정 아트웍에 속한 퍼즐 목록을 조회
    @GetMapping("/{artworkUid}/puzzles")
    public ResponseEntity<List<PuzzleResponseDTO>> getPuzzlesByArtworkUid(@PathVariable String artworkUid) {
        List<PuzzleResponseDTO> puzzleDto = artworkService.getPuzzlesByArtworkUid(artworkUid);
        return ResponseEntity.ok(puzzleDto);

    }
}

