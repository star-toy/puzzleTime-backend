package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startoy.puzzletime.domain.Puzzle;
import startoy.puzzletime.domain.PuzzlePlay;
import startoy.puzzletime.dto.puzzlePlay.PlayingPuzzlesResponseDTO;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayRequest;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayResponse;
import startoy.puzzletime.repository.PuzzlePlayRepository;
import startoy.puzzletime.repository.PuzzleRepository;
import startoy.puzzletime.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PuzzlePlayService {

    private static final Logger logger = LoggerFactory.getLogger(PuzzlePlayService.class);
    private final PuzzlePlayRepository puzzlePlayRepository;
    private final PuzzleRepository puzzleRepository;
    private final UserRepository userRepository;

    @Transactional
    public PuzzlePlayResponse savePuzzlePlay(String puzzlePlayUID, long userId, PuzzlePlayRequest request) {

        Puzzle puzzle = puzzleRepository.findByPuzzleUid(request.getPuzzleUid()).get();

        // 기존 퍼즐 진행 조회 또는 생성
        PuzzlePlay puzzlePlay = puzzlePlayRepository.findByPuzzlePlayUid(puzzlePlayUID)
                .orElseGet(() -> PuzzlePlay.builder()
                        .puzzlePlayUid(puzzlePlayUID)
                        .puzzle(puzzle)
                        .user(userRepository.findById(userId).get())
                        .createdAt(LocalDateTime.now())
                        .build());

        // 진행 상태 업데이트
        puzzlePlay.setPuzzlePlayData(request.getPuzzlePlayData());
        puzzlePlay.setIsCompleted(request.isCompleted());
        puzzlePlay.setUpdatedAt(LocalDateTime.now());

        // 저장
        PuzzlePlay savedPuzzlePlay = puzzlePlayRepository.save(puzzlePlay);

        // Artwork 내 퍼즐 완료 현황 계산
        String completedPuzzlesFraction = puzzlePlayRepository.getCompletedPuzzlesFractionByUid(userId, puzzle.getPuzzleId());

        return PuzzlePlayResponse.builder()
                .puzzlePlayUid(savedPuzzlePlay.getPuzzlePlayUid())
                .puzzleUid(savedPuzzlePlay.getPuzzle().getPuzzleUid())
                .puzzlePlayData(savedPuzzlePlay.getPuzzlePlayData())
                .isCompleted(savedPuzzlePlay.getIsCompleted())
                .completedPuzzlesFraction(completedPuzzlesFraction)
                .build();

    }

    // 사용자 이메일 기반으로 미완성 퍼즐 조회
    public PlayingPuzzlesResponseDTO getPlayingPuzzles(String userEmail) {

        logger.info("Fetching playing puzzles for user: {}", userEmail);

        // 플레이 중인 퍼즐 정보 조회
        List<PuzzlePlay> playingPuzzles = puzzlePlayRepository.findPlayingPuzzlesByUserEmail(userEmail);

        // 아트웍별로 그룹화
        Map<String, List<PuzzlePlay>> groupedByArtwork = playingPuzzles.stream()
                .collect(Collectors.groupingBy(puzzlePlay -> puzzlePlay.getPuzzle().getArtwork().getArtworkUid()));

        // 그룹화된 데이터를 DTO로 변환
        List<PlayingPuzzlesResponseDTO.ArtworkWithPuzzles> artworkWithPuzzlesList = groupedByArtwork.entrySet()
                .stream()
                .map(entry -> {
                    // 첫 번째 PuzzlePlay에서 Artwork 정보를 가져옴
                    String imageUrl = entry.getValue().get(0).getPuzzle().getArtwork().getArtworkImage().getImageUrl();

                    return PlayingPuzzlesResponseDTO.ArtworkWithPuzzles.builder()
                            .artworkUid(entry.getKey())
                            .imageUrl(imageUrl) // 아트웍 이미지 URL 설정
                            .puzzles(entry.getValue().stream()
                                    .map(this::convertToPuzzleDTO)
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());

        return PlayingPuzzlesResponseDTO.builder()
                .artworks(artworkWithPuzzlesList)
                .build();
    }

    private PlayingPuzzlesResponseDTO.PuzzleDTO convertToPuzzleDTO(PuzzlePlay puzzlePlay) {
        return PlayingPuzzlesResponseDTO.PuzzleDTO.builder()
                .puzzleUid(puzzlePlay.getPuzzle().getPuzzleUid())
                .puzzleIndex(puzzlePlay.getPuzzle().getPuzzleIndex())
                .imageUrl(puzzlePlay.getPuzzle().getPuzzleImage().getImageUrl())
                .completed(puzzlePlay.getIsCompleted() != null && puzzlePlay.getIsCompleted())
                .build();
    }
}
