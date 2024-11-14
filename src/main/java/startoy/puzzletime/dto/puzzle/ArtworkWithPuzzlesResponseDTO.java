package startoy.puzzletime.dto.puzzle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import startoy.puzzletime.dto.ArtworkDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkWithPuzzlesResponseDTO {
    private ArtworkDto artwork; // 아트웍 기본 정보
    private List<PuzzleResponseDTO> puzzles; // 퍼즐 목록
}