package startoy.puzzletime.dto.puzzlePlay;


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
    public static class PuzzleDTO {
        private String puzzleUid;
        private int puzzleIndex;
        private String imageUrl;
        private boolean completed;
    }
}
