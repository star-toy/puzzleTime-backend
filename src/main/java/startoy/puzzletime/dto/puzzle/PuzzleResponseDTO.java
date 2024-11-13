package startoy.puzzletime.dto.puzzle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleResponseDTO {
    private String puzzleUid;      // 퍼즐의 UID
    private int puzzleIndex;       // 퍼즐의 순서 (인덱스)
    private Long puzzleImageId;    // 퍼즐 이미지의 ID
    private String imageUrl;       // 퍼즐 이미지의 URL
    //private boolean isCompleted; // 완료 여부를 포함
}