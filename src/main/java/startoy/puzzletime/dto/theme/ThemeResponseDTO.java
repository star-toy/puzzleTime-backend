package startoy.puzzletime.dto.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import startoy.puzzletime.dto.artwork.ArtworkDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeResponseDTO {
    private ThemeWithArtworksResponseDTO theme; // 테마 정보
    private List<ArtworkDTO> artworks; // 아트웍 + 퍼즐 정보
    private String email; // 사용자 이메일 (선택)
}
