package startoy.puzzletime.dto.puzzle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPuzzleResponse {
    private String puzzleUid;
    private String imageUrl;
    private String puzzlePlayUid;
    private String userId;
    private boolean isCompleted;
    private int piecesCount;
    private String piecesData;
}
