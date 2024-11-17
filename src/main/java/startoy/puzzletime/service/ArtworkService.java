package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
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

    private final ArtworkRepository artworkRepository;
    private final PuzzleRepository puzzleRepository;
    private final ImageStorageRepository imageStorageRepository;
    private final PuzzlePlayRepository puzzlePlayRepository;

    // artworkUid로 특정 Artwork를 조회하는 메서드
    public ArtworkDTO getArtworkByUid(String artworkUid) {
        return artworkRepository.findByArtworkUid(artworkUid)
                .map(artwork -> new ArtworkDTO(
                        artwork.getArtworkUid(),
                        artwork.getArtworkTitle(),
                        artwork.getArtworkDescription()
                ))
                .orElseThrow(() -> new CustomException(ErrorCode.ARTWORK_NOT_FOUND));
    }

    /**
     * artworkUid로 해당 아트웍의 정보와 퍼즐 목록을 포함하여 조회합니다.
     */
    public ArtworkWithPuzzlesResponseDTO getArtworkWithPuzzlesByUid(String artworkUid) {
        // artworkUid로 Artwork 엔티티 조회. 없으면 예외 발생
        Artwork artwork = artworkRepository.findByArtworkUid(artworkUid)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTWORK_NOT_FOUND));

        // 아트웍 정보 생성
        ArtworkDTO artworkDto = new ArtworkDTO(
                artwork.getArtworkUid(),
                artwork.getArtworkTitle(),
                artwork.getArtworkDescription()
        );

        // artwork_id로 퍼즐 정보 조회 후, PuzzleDto로 변환
        List<PuzzleResponseDTO> puzzles = puzzleRepository.findByArtworkArtworkId(artwork.getArtworkId())
                .stream()
                .map(puzzle -> {
                    // 퍼즐의 이미지 URL을 가져오기 위해 imageStorageRepository에서 조회
                    String imageUrl = imageStorageRepository.findById(puzzle.getPuzzleImage().getImageId())
                            .map(ImageStorage::getImageUrl)
                            .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));

                    // tb_puzzle_play 테이블에서 해당 퍼즐의 완료 여부를 조회
                    boolean isCompleted = puzzlePlayRepository.findByPuzzle_PuzzleId(puzzle.getPuzzleId())
                            .map(PuzzlePlay::getIsCompleted)
                            .orElse(false);

                    // PuzzleResponseDTO 객체 생성 및 반환
                    return new PuzzleResponseDTO(
                            puzzle.getPuzzleUid(),
                            puzzle.getPuzzleIndex(),
                            puzzle.getPuzzleImage().getImageId(),
                            imageUrl,
                            isCompleted
                    );
                })
                .collect(Collectors.toList());

        // ArtworkWithPuzzlesResponseDTO 생성 및 반환
        return new ArtworkWithPuzzlesResponseDTO(artworkDto, puzzles);
    }
}