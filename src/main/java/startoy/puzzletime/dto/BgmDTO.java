package startoy.puzzletime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BgmDTO {
    private Long bgmId;        // BGM ID
    private String bgmTitle;   // BGM 제목
    private String bgmUrl;     // BGM URL
}
