package startoy.puzzletime.dto.artwork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import startoy.puzzletime.dto.puzzle.PuzzleResponseDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkWithPuzzlesDTO {
    private String artworkUid; // 아트웍 UID
    private String artworkTitle; // 아트웍 제목
    private String artworkDescription; // 아트웍 설명
    private String imageUrl; // 아트웍 이미지 URL
    private Integer artworkSeq; // 아트웍 순서
    private List<PuzzleResponseDTO> puzzles; // 퍼즐 리스트
}
