package startoy.puzzletime.dto.puzzlePlay;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PuzzlePlayRequest {
    @NotNull
    private String puzzlePlayData;
    @NotNull
    private String puzzleUid;
    @NotNull
    @JsonProperty("isCompleted")
    private boolean isCompleted;
}
