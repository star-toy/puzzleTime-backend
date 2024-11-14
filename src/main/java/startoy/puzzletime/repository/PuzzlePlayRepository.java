package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startoy.puzzletime.domain.PuzzlePlay;

import java.util.Optional;

@Repository
public interface PuzzlePlayRepository extends JpaRepository<PuzzlePlay, Long> {

    // 퍼즐 ID로 해당 퍼즐 플레이 정보를 조회
    Optional<PuzzlePlay> findByPuzzle_PuzzleId(Long puzzleId);
}
