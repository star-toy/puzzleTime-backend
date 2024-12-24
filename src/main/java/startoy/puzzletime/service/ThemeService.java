package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import startoy.puzzletime.domain.ImageStorage;
import startoy.puzzletime.domain.PuzzlePlay;
import startoy.puzzletime.domain.Theme;
import startoy.puzzletime.dto.artwork.ArtworkDTO;
import startoy.puzzletime.dto.BgmDTO;
import startoy.puzzletime.dto.artwork.ArtworkWithImageDTO;
import startoy.puzzletime.dto.artwork.ArtworkWithPuzzlesDTO;
import startoy.puzzletime.dto.puzzle.PuzzleResponseDTO;
import startoy.puzzletime.dto.theme.ThemeWithArtworksAndPuzzlesResponseDTO;
import startoy.puzzletime.dto.theme.ThemeWithArtworksResponseDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private static final Logger logger = LoggerFactory.getLogger(ThemeService.class);
    private final ThemeRepository themeRepository;
    private final ArtworkRepository artworkRepository;
    private final ImageStorageRepository imageStorageRepository;
    private final PuzzleRepository puzzleRepository;
    private final PuzzlePlayRepository puzzlePlayRepository;
    private final UserService userService;


     //모든 테마와 해당 테마의 아트웍 리스트를 가져오는 메서드
    public List<ThemeWithArtworksResponseDTO> getAllThemesWithArtworks() {
        return themeRepository.findAll()
                .stream()
                .map(this::convertThemeToResponseDTO)
                .collect(Collectors.toList());
    }

    // 특정 테마 UID에 대한 테마, 아트웍, 퍼즐 정보를 가져오는 메서드
    public ThemeWithArtworksAndPuzzlesResponseDTO getThemeWithArtworksAndPuzzlesByUid(String themeUid, String userEmail) {
        // 테마 조회
        Theme theme = themeRepository.findByThemeUid(themeUid)
                .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));

        // BGM 정보 변환
        BgmDTO bgmDTO = new BgmDTO(
                theme.getBgm().getSoundId(),
                theme.getBgm().getSoundTitle(),
                theme.getBgm().getSoundUrl()
        );

        // userId로 변경
        Long userId;
        if (userEmail != null) {
            userId = userService.getUserIdByEmail(userEmail); // userEmail -> userId 변환
            logger.info("userId: {}", userId);
        } else {
            userId = null;
        }

        // 테마에 속한 아트웍과 퍼즐 정보 조회
        List<ArtworkWithPuzzlesDTO> artworks = artworkRepository.findByThemeThemeId(theme.getThemeId())
                .stream()
                .map(artwork -> {
                    // 아트웍의 퍼즐 정보 생성
                    List<PuzzleResponseDTO> puzzles = puzzleRepository.findByArtworkArtworkId(artwork.getArtworkId())
                            .stream()
                            .map(puzzle -> {
                                // 퍼즐 완료 여부 확인
                                boolean isCompleted = false;

                                if (userId != null) {
                                    // 최신 플레이 기록을 기준으로 완료 여부 확인
                                    List<PuzzlePlay> puzzlePlays = puzzlePlayRepository
                                            .findByPuzzle_PuzzleIdAndUser_Id(puzzle.getPuzzleId(), userId);

                                    // puzzlePlays가 비어있으면 (플레이 기록이 없으면) false 유지
                                    // 플레이 기록이 있으면 완료 여부 확인
                                    isCompleted = !puzzlePlays.isEmpty() &&
                                            puzzlePlays.stream().anyMatch(PuzzlePlay::getIsCompleted);
                                }

                                // PuzzleResponseDTO 생성
                                return new PuzzleResponseDTO(
                                        puzzle.getPuzzleUid(),
                                        puzzle.getPuzzleIndex(),
                                        puzzle.getPuzzleImage().getImageId(),
                                        puzzle.getPuzzleImage().getImageUrl(),
                                        isCompleted
                                );
                            })
                            .collect(Collectors.toList());

                    // ArtworkWithPuzzlesDTO 생성
                    return new ArtworkWithPuzzlesDTO(
                            artwork.getArtworkUid(),
                            artwork.getArtworkTitle(),
                            artwork.getArtworkDescription(),
                            artwork.getArtworkImage().getImageUrl(),
                            artwork.getArtworkSeq(),
                            puzzles
                    );
                })
                .collect(Collectors.toList());

        // 최종 응답 DTO 생성 및 반환
        return new ThemeWithArtworksAndPuzzlesResponseDTO(
                theme.getThemeUid(),
                theme.getThemeTitle(),
                theme.getImage().getImageUrl(),
                bgmDTO,
                artworks,
                userEmail
        );
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
        String imageUrl = theme.getImage().getImageUrl();

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
                imageUrl,
                bgmDTO,
                artworks
        );
    }
}