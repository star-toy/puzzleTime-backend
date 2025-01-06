package startoy.puzzletime.dto.puzzle;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private int puzzleIndex;       // 퍼즐의 순서 (인덱스)
    private Long puzzleImageId;    // 퍼즐 이미지의 ID
    private String ImageUrl;
    private String puzzlePlayUid;
    private String puzzlePlayData;

    @JsonProperty("isCompleted")   // JSON에서 필드 이름을 "isCompleted"로 설정 (미적용시 json 응답에 필드명이 completed 로 바뀜)
    private boolean isCompleted;   // 퍼즐 완료 여부
}
