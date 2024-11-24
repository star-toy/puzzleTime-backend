package startoy.puzzletime.repository;

import org.springframework.stereotype.Repository;
import startoy.puzzletime.domain.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {

    // artwork_id로 퍼즐 리스트 조회
    List<Puzzle> findByArtworkArtworkId(Long artworkId);

    Optional<Puzzle> findByPuzzleUid(String puzzleUid);
}