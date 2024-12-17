package startoy.puzzletime.dto.artwork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkDTO {
    private String artworkUid;
    private String artworkTitle;
    private String artworkDescription;
    private Integer artworkSeq; // 순서 정보 추가
}
