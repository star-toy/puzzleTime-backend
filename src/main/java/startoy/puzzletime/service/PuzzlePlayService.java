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

    // 사용자 이메일 기반으로 플레이 이력이 있는 아트웍의 퍼즐 조회 (완성된 퍼즐 제외)
    public PlayingPuzzlesResponseDTO getPlayingPuzzles(String userEmail) {
        logger.info("Fetching playing puzzles for user: {}", userEmail);

        // 1. 플레이 이력이 있는 아트웍 UID 가져오기
        List<String> artworkUids = puzzleRepository.findDistinctArtworkUidsByUserEmail(userEmail);

        // 2. 각 아트웍에 대한 퍼즐과 상태 가져오기
        List<Object[]> puzzlesWithStatus = artworkUids.stream()
                .flatMap(artworkUid -> puzzleRepository.findAllPuzzlesWithPlayStatusByArtworkUidAndUserEmail(artworkUid, userEmail).stream())
                .collect(Collectors.toList());

        // 3. 아트웍별로 그룹화
        Map<String, List<Object[]>> groupedByArtwork = puzzlesWithStatus.stream()
                .collect(Collectors.groupingBy(row -> ((Puzzle) row[0]).getArtwork().getArtworkUid()));

        // 4. 그룹화된 데이터를 "완성된 퍼즐"과 "플레이 중인 퍼즐"로 나누기
        List<PlayingPuzzlesResponseDTO.ArtworkWithPuzzles> playingPuzzles = groupedByArtwork.entrySet().stream()
                .filter(entry -> {
                    // 모든 퍼즐이 완료되지 않은 경우만 플레이 중인 퍼즐로 포함
                    List<Object[]> puzzles = entry.getValue();
                    long completedCount = puzzles.stream()
                            .filter(row -> row[1] != null && Boolean.TRUE.equals(((PuzzlePlay) row[1]).getIsCompleted()))
                            .count();
                    return completedCount < puzzles.size(); // 모든 퍼즐이 완료되지 않은 경우
                })
                .map(entry -> {
                    // 아트웍 정보
                    String artworkUid = entry.getKey();
                    String artworkImageUrl = entry.getValue().stream()
                            .findFirst()
                            .map(row -> ((Puzzle) row[0]).getArtwork().getArtworkImage().getImageUrl())
                            .orElse("");

                    // 퍼즐 리스트 생성
                    List<PlayingPuzzlesResponseDTO.PuzzleDTO> puzzles = entry.getValue().stream()
                            .map(row -> {
                                Puzzle puzzle = (Puzzle) row[0];
                                PuzzlePlay puzzlePlay = (PuzzlePlay) row[1];

                                return PlayingPuzzlesResponseDTO.PuzzleDTO.builder()
                                        .puzzleUid(puzzle.getPuzzleUid())
                                        .puzzleIndex(puzzle.getPuzzleIndex())
                                        .imageUrl(puzzle.getPuzzleImage().getImageUrl())
                                        .isCompleted(puzzlePlay != null && Boolean.TRUE.equals(puzzlePlay.getIsCompleted())) // null일 경우 false
                                        .build();
                            })
                            .collect(Collectors.toList());

                    return PlayingPuzzlesResponseDTO.ArtworkWithPuzzles.builder()
                            .artworkUid(artworkUid)
                            .imageUrl(artworkImageUrl)
                            .puzzles(puzzles)
                            .build();
                })
                .collect(Collectors.toList());

        return PlayingPuzzlesResponseDTO.builder()
                .artworks(playingPuzzles)
                .build();
    }

}
