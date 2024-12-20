package startoy.puzzletime.dto.artwork;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompleteArtworksResponse {
    private int artworkSeq;
    private String artworkUid;
    private String artworkImgUrl;
    private String rewardImgUrl;
}