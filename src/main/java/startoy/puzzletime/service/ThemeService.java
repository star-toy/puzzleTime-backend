package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import startoy.puzzletime.domain.ImageStorage;
import startoy.puzzletime.domain.Theme;
import startoy.puzzletime.dto.artwork.ArtworkDTO;
import startoy.puzzletime.dto.BgmDTO;
import startoy.puzzletime.dto.artwork.ArtworkWithImageDTO;
import startoy.puzzletime.dto.theme.ThemeWithArtworksResponseDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.repository.ArtworkRepository;
import startoy.puzzletime.repository.ImageStorageRepository;
import startoy.puzzletime.repository.ThemeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ArtworkRepository artworkRepository;
    private final ImageStorageRepository imageStorageRepository;


     //모든 테마와 해당 테마의 아트웍 리스트를 가져오는 메서드
    public List<ThemeWithArtworksResponseDTO> getAllThemesWithArtworks() {
        return themeRepository.findAll()
                .stream()
                .map(this::convertThemeToResponseDTO)
                .collect(Collectors.toList());
    }


    //특정 테마 UID에 해당하는 테마와 아트웍 리스트를 가져오는 메서드
    public ThemeWithArtworksResponseDTO getThemeWithArtworksByUid(String themeUid) {

        // 테마를 themeUid로 조회, 없으면 예외 발생
        Theme theme = themeRepository.findByThemeUid(themeUid)
                .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));
        return convertThemeToResponseDTO(theme);
    }


    //Theme 엔티티를 ThemeWithArtworksResponseDTO로 변환하는 공통 메서드
    private ThemeWithArtworksResponseDTO convertThemeToResponseDTO(Theme theme) {

        // 해당 테마의 이미지 URL 조회
        String themeImageUrl = theme.getImage().getImageUrl();

        // 해당 테마에 속한 아트웍 리스트 조회
        List<ArtworkWithImageDTO> artworks = artworkRepository.findByThemeThemeId(theme.getThemeId())
                .stream()
                .map(artwork -> {
                    // 아트웍 이미지 URL 조회
                    String artworkImageUrl = imageStorageRepository.findById(artwork.getArtworkImage().getImageId())
                            .map(ImageStorage::getImageUrl)
                            .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));

                    // ArtworkWithImageDTO 생성
                    return new ArtworkWithImageDTO(
                            artwork.getArtworkUid(),
                            artwork.getArtworkTitle(),
                            artwork.getArtworkDescription(),
                            artwork.getArtworkSeq(),
                            artworkImageUrl
                    );
                })
                .collect(Collectors.toList());

        // BGM 정보를 BgmDTO로 변환
        BgmDTO bgmDTO = new BgmDTO(
                theme.getBgm().getSoundId(),
                theme.getBgm().getSoundTitle(),
                theme.getBgm().getSoundUrl()
        );

        // ThemeWithArtworksResponseDTO 반환
        return new ThemeWithArtworksResponseDTO(
                theme.getThemeUid(),
                theme.getThemeTitle(),
                themeImageUrl,
                bgmDTO,
                artworks
        );
    }
}