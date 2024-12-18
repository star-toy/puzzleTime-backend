package startoy.puzzletime.dto.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import startoy.puzzletime.dto.artwork.ArtworkDTO;
import startoy.puzzletime.dto.BgmDTO;
import startoy.puzzletime.dto.artwork.ArtworkWithImageDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeWithArtworksResponseDTO {
    private String themeUid;      // 테마 UID
    private String themeTitle;   // 테마 제목
    private String imageUrl;   // 테마 이미지 URL
    private BgmDTO bgm;                  // BGM 정보를 담은 DTO
    private List<ArtworkWithImageDTO> artworks; // 테마에 포함된 아트웍 리스트
}
