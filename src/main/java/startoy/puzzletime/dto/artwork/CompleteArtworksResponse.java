package startoy.puzzletime.dto.artwork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompleteArtworksResponse {
    private String artworkUid;
    private String artworkImgUrl;
    private String rewardImgUrl;
}