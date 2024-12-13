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
    public PuzzlePlayResponse savePuzzlePlay(String puzzleUid, String userId, PuzzlePlayRequest request) {
        // 사용자 및 퍼즐 확인
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Puzzle puzzle = puzzleRepository.findByPuzzleUid(puzzleUid)
                .orElseThrow(() -> new IllegalArgumentException("Puzzle not found"));

        // 기존 퍼즐 진행 상태 조회
        PuzzlePlay puzzlePlay = puzzlePlayRepository.findByUserAndPuzzle(user, puzzle)
                .orElseGet(() -> PuzzlePlay.builder()
                        .puzzlePlayUid(UUID.randomUUID().toString())
                        .puzzle(puzzle)
                        .user(user)
                        .isCompleted(false)
                        .createdAt(LocalDateTime.now())
                        .build());

        // 진행 상태 업데이트
        puzzlePlay.setPuzzlePlayData(request.getPuzzlePlayData());
        puzzlePlay.setUpdatedAt(LocalDateTime.now());

        // 저장
        PuzzlePlay savedPuzzlePlay = puzzlePlayRepository.save(puzzlePlay);
        
        return PuzzlePlayResponse.builder()
                .puzzlePlayUid(savedPuzzlePlay.getPuzzlePlayUid())
                .puzzleUid(savedPuzzlePlay.getPuzzle().getPuzzleUid())
                .userId(savedPuzzlePlay.getUser().getId())
                .isCompleted(savedPuzzlePlay.getIsCompleted())
                .puzzlePlayData(savedPuzzlePlay.getPuzzlePlayData())
                .build();
    }
}
