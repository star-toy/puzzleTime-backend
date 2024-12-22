package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startoy.puzzletime.domain.Puzzle;
import startoy.puzzletime.domain.PuzzlePlay;
import startoy.puzzletime.domain.UserArtwork;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayRequest;
import startoy.puzzletime.dto.puzzlePlay.PuzzlePlayResponse;
import startoy.puzzletime.repository.PuzzlePlayRepository;
import startoy.puzzletime.repository.PuzzleRepository;
import startoy.puzzletime.repository.UserArtworkRepository;
import startoy.puzzletime.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PuzzlePlayService {

    private final PuzzlePlayRepository puzzlePlayRepository;
    private final PuzzleRepository puzzleRepository;
    private final UserRepository userRepository;
    private final UserArtworkRepository userArtworkRepository;
    private final UserService userService;

    @Transactional
    public PuzzlePlayResponse savePuzzlePlay(String puzzlePlayUID, Long userId, PuzzlePlayRequest request) {

        Puzzle puzzle = puzzleRepository.findByPuzzleUid(request.getPuzzleUid()).get();

        // 기존 퍼즐 진행 조회 또는 생성
        PuzzlePlay puzzlePlay = puzzlePlayRepository.findByPuzzlePlayUid(puzzlePlayUID)
                .orElseGet(() -> PuzzlePlay.builder()
                        .puzzlePlayUid(puzzlePlayUID)
                        .puzzle(puzzle)
                        .user(!userId.equals(userService.GUEST_USER_ID) ? userRepository.findById(userId).get() : null) // 게스트 유저 처리
                        .createdAt(LocalDateTime.now())
                        .build());

        // 진행 상태 업데이트
        puzzlePlay.setPuzzlePlayData(request.getPuzzlePlayData());
        puzzlePlay.setIsCompleted(request.isCompleted());
        puzzlePlay.setUpdatedAt(LocalDateTime.now());

        if (puzzlePlay.getUser() == null && !userId.equals(userService.GUEST_USER_ID)) { // 기존 정보가 게스트 정보이고, 현재 로그인 상태라면
            puzzlePlay.setUser(userRepository.findById(userId).get()); // 회원 정보 업데이트
        }

        // Artwork 내 퍼즐 완료 현황 계산
        String completedPuzzlesFraction;
        if (!userId.equals(userService.GUEST_USER_ID)) { // 회원인 경우
            // 퍼즐 완료 현황 계산
            Map<String, String> puzzleFractionInfo = puzzlePlayRepository.getCompletedPuzzlesFractionByUid(userId, puzzle.getPuzzleId());
            completedPuzzlesFraction = String.format("%s/%s", puzzleFractionInfo.get("completedPuzzleCount"), puzzleFractionInfo.get("totalPuzzleCount"));

            // 모든 퍼즐 완성 시 UserArtwork 저장
            if (puzzleFractionInfo.get("completedPuzzleCount").equals(puzzleFractionInfo.get("totalPuzzleCount"))) {
                UserArtwork userArtwork = UserArtwork.builder()
                        .userArtworkUid(UUID.randomUUID().toString())
                        .artwork(puzzle.getArtwork())
                        .user(userRepository.findById(userId).get())
                        .isCompleted(request.isCompleted())
                        .build();
                userArtworkRepository.save(userArtwork);
            }
        } else { // 게스트인 경우
            completedPuzzlesFraction = request.isCompleted() ? "1/4" : "0/4";
        }

        return PuzzlePlayResponse.builder()
                .puzzlePlayUid(puzzlePlay.getPuzzlePlayUid())
                .puzzleUid(puzzlePlay.getPuzzle().getPuzzleUid())
                .puzzlePlayData(puzzlePlay.getPuzzlePlayData())
                .isCompleted(puzzlePlay.getIsCompleted())
                .completedPuzzlesFraction(completedPuzzlesFraction)
                .build();
    }

}
