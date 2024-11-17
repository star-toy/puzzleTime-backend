package startoy.puzzletime.dto.artwork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkWithImageDTO {
    private String artworkUid;       // 아트웍 UID
    private String artworkTitle;     // 아트웍 제목
    private String artworkDescription; // 아트웍 설명
    private String imageUrl;         // 아트웍 이미지 URL
}
