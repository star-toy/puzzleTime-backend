package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import startoy.puzzletime.domain.Artwork;
import startoy.puzzletime.domain.ImageStorage;
import startoy.puzzletime.domain.PuzzlePlay;
import startoy.puzzletime.dto.artwork.ArtworkDTO;
import startoy.puzzletime.dto.puzzle.PuzzleResponseDTO;
import startoy.puzzletime.exception.CustomException;
import startoy.puzzletime.exception.ErrorCode;
import startoy.puzzletime.repository.*;
import java.util.List;
import java.util.stream.Collectors;
import startoy.puzzletime.dto.puzzle.ArtworkWithPuzzlesResponseDTO;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private static final Logger logger = LoggerFactory.getLogger(ArtworkService.class);
    private final ArtworkRepository artworkRepository;
    private final PuzzleRepository puzzleRepository;
    private final ImageStorageRepository imageStorageRepository;
    private final PuzzlePlayRepository puzzlePlayRepository;
    private final UserService userService;

    // artworkUid로 특정 Artwork를 조회하는 메서드
    public ArtworkDTO getArtworkByUid(String artworkUid) {
        return artworkRepository.findByArtworkUid(artworkUid)
                .map(artwork -> new ArtworkDTO(
                        artwork.getArtworkUid(),
                        artwork.getArtworkTitle(),
                        artwork.getArtworkDescription(),
                        artwork.getArtworkSeq()
                ))
                .orElseThrow(() -> new CustomException(ErrorCode.ARTWORK_NOT_FOUND));
    }

    /**
     * artworkUid와 사용자 이메일로 아트웍 정보와 퍼즐 목록을 포함하여 조회
     */
    public ArtworkWithPuzzlesResponseDTO getArtworkWithPuzzlesByUidAndUser(String artworkUid, String userEmail) {
        // artworkUid로 Artwork 엔티티 조회. 없으면 예외 발생
        Artwork artwork = artworkRepository.findByArtworkUid(artworkUid)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTWORK_NOT_FOUND));

        // 아트웍 정보 생성
        ArtworkDTO artworkDto = new ArtworkDTO(
                artwork.getArtworkUid(),
                artwork.getArtworkTitle(),
                artwork.getArtworkDescription(),
                artwork.getArtworkSeq()
        );

        // userId로 변경
        Long userId;
        if (userEmail != null) {
            userId = userService.getUserIdByEmail(userEmail); // userEmail -> userId 변환
            logger.info("userId: {}", userId);
        } else {
            userId = null;
        }

        // artwork_id로 퍼즐 정보 조회 후, PuzzleDto로 변환
        List<PuzzleResponseDTO> puzzles = puzzleRepository.findByArtworkArtworkId(artwork.getArtworkId())
                .stream()
                .map(puzzle -> {
                    String imageUrl = imageStorageRepository.findById(puzzle.getPuzzleImage().getImageId())
                            .map(ImageStorage::getImageUrl)
                            .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));

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

                    return new PuzzleResponseDTO(
                            puzzle.getPuzzleUid(),
                            puzzle.getPuzzleIndex(),
                            puzzle.getPuzzleImage().getImageId(),
                            imageUrl,
                            isCompleted
                    );
                })
                .collect(Collectors.toList());

        return new ArtworkWithPuzzlesResponseDTO(artworkDto, puzzles, userEmail);
    }
}