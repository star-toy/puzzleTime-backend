package startoy.puzzletime.dto.myPage;


import lombok.*;

import java.util.List;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPagePlayingPuzzlesResponseDTO {
    private List<ArtworkWithPuzzles> artworks;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArtworkWithPuzzles {
        private String artworkUid;
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
