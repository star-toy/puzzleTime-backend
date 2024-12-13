package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startoy.puzzletime.domain.Puzzle;
import startoy.puzzletime.domain.PuzzlePlay;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayRequest;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayResponse;
import startoy.puzzletime.repository.PuzzlePlayRepository;
import startoy.puzzletime.repository.PuzzleRepository;
import startoy.puzzletime.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PuzzlePlayService {

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
}
