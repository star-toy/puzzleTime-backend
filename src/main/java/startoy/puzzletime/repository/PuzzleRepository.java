package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT DISTINCT p.artwork.artworkUid FROM PuzzlePlay pp JOIN pp.puzzle p JOIN pp.user u WHERE u.email = :email")
    List<String> findDistinctArtworkUidsByUserEmail(String email);

    // 특정 아트웍의 퍼즐과 플레이 상태 조회
    @Query("SELECT p, pp " +
            "FROM Puzzle p " +
            "LEFT JOIN PuzzlePlay pp ON p = pp.puzzle AND pp.user.email = :email " +
            "WHERE p.artwork.artworkUid = :artworkUid")
    List<Object[]> findAllPuzzlesWithPlayStatusByArtworkUidAndUserEmail(
            @Param("artworkUid") String artworkUid,
            @Param("email") String email);

}