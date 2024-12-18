package startoy.puzzletime.dto.puzzle;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String ImageUrl;       // 퍼즐 이미지의 URL

    @JsonProperty("isCompleted")   // JSON에서 필드 이름을 "isCompleted"로 설정 (미적용시 json 응답에 필드명이 completed 로 바뀜)
    private boolean isCompleted;    // 퍼즐 완료 여부
}