package startoy.puzzletime.dto.puzzlePlay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PuzzlePlayResponse {
    private String puzzlePlayUid;
    private String puzzleUid;
    private String puzzlePlayData;
    @JsonProperty("isCompleted")
    private boolean isCompleted;
    private String completedPuzzlesFraction; // "완료한 퍼즐 수/총 퍼즐 수" : "1/4", "2/4",,,
}