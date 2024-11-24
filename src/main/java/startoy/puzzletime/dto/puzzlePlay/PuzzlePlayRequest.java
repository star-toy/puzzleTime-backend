package startoy.puzzletime.dto.puzzlePlay;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PuzzlePlayRequest {
    @NotNull
    private String piecesData;
}
