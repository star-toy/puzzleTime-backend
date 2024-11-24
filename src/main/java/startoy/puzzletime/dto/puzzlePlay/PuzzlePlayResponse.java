package startoy.puzzletime.dto.puzzlePlay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PuzzlePlayResponse {
    private String puzzlePlayUid;
    private String puzzleUid;
    private Long userId;
    private boolean isCompleted;
    private String piecesData;
}