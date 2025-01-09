package startoy.puzzletime.dto.puzzlePlay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class PuzzlePlayRequest {
    @NotNull
    @Schema(description = "퍼즐의 각 피스 상태를 저장하는 데이터. 예: {\"piece_1\": \"data\", \"piece_2\": \"data\"}")
    private Map<String, Object> puzzlePlayData;
    @NotNull
    private String puzzleUid;
    @NotNull
    @JsonProperty("isCompleted")
    private boolean isCompleted;
}
