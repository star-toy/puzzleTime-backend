package startoy.puzzletime.dto.puzzlePlay;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayingPuzzlesResponseDTO {
    private List<ArtworkWithPuzzles> artworks;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArtworkWithPuzzles {
        private String artworkUid;
        private String imageUrl;
        private List<PuzzleDTO> puzzles;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(value = { "completed" }) // "completed" 필드를 무시
    public static class PuzzleDTO {
        private String puzzleUid;
        private int puzzleIndex;
        private String imageUrl;
        @JsonProperty("isCompleted")
        private boolean isCompleted;
    }
}
