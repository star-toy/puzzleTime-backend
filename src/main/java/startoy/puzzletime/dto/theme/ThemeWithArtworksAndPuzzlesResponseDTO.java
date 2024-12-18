package startoy.puzzletime.dto.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import startoy.puzzletime.dto.BgmDTO;
import startoy.puzzletime.dto.artwork.ArtworkWithPuzzlesDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeWithArtworksAndPuzzlesResponseDTO {
    private String themeUid; // 테마 UID
    private String themeTitle; // 테마 제목
    private String imageUrl; // 테마 이미지 URL
    private BgmDTO bgm; // BGM 정보를 담은 DTO
    private List<ArtworkWithPuzzlesDTO> artworks; // 퍼즐 정보를 포함한 아트웍 리스트
    private String email; // 사용자 이메일 (선택)
}
