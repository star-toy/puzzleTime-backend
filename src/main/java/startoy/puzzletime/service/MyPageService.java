package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import startoy.puzzletime.dto.puzzlePlay.PlayingPuzzlesResponseDTO;
import startoy.puzzletime.domain.PuzzlePlay;
import startoy.puzzletime.repository.PuzzlePlayRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {

    private static final Logger logger = LoggerFactory.getLogger(MyPageService.class);
    private final PuzzlePlayRepository puzzlePlayRepository;


     // 사용자 이메일 기반으로 미완성 퍼즐 조회
   /* public MyPagePlayingPuzzlesResponseDTO getPlayingPuzzles(String userEmail) {
        logger.info("Fetching playing puzzles for user: {}", userEmail);

        // 플레이 중인 퍼즐 정보 조회
        List<PuzzlePlay> playingPuzzles = puzzlePlayRepository.findPlayingPuzzlesByUserEmail(userEmail);

        // 아트웍별로 그룹화
        Map<String, List<PuzzlePlay>> groupedByArtwork = playingPuzzles.stream()
                .collect(Collectors.groupingBy(puzzlePlay -> puzzlePlay.getPuzzle().getArtwork().getArtworkUid()));

        // 그룹화된 데이터를 DTO로 변환
        List<MyPagePlayingPuzzlesResponseDTO.ArtworkWithPuzzles> artworkWithPuzzlesList = groupedByArtwork.entrySet()
                .stream()
                .map(entry -> MyPagePlayingPuzzlesResponseDTO.ArtworkWithPuzzles.builder()
                        .artworkUid(entry.getKey())
                        .puzzles(entry.getValue().stream()
                                .map(this::convertToPuzzleDTO)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return MyPagePlayingPuzzlesResponseDTO.builder()
                .artworks(artworkWithPuzzlesList)
                .build();
    }

    private MyPagePlayingPuzzlesResponseDTO.PuzzleDTO convertToPuzzleDTO(PuzzlePlay puzzlePlay) {
        return MyPagePlayingPuzzlesResponseDTO.PuzzleDTO.builder()
                .puzzleUid(puzzlePlay.getPuzzle().getPuzzleUid())
                .puzzleIndex(puzzlePlay.getPuzzle().getPuzzleIndex())
                .imageUrl(puzzlePlay.getPuzzle().getPuzzleImage().getImageUrl())
                .completed(puzzlePlay.getIsCompleted() != null && puzzlePlay.getIsCompleted())
                .build();
    }*/
}
