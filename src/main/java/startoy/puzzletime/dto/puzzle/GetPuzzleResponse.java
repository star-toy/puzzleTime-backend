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
    private String ImageUrl;
    private String puzzlePlayUid;
    private String puzzlePlayData;
}
