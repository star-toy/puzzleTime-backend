package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import startoy.puzzletime.domain.Puzzle;
import startoy.puzzletime.domain.PuzzlePlay;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.dto.puzzle.GetPuzzleResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PuzzlePlayRepository extends JpaRepository<PuzzlePlay, Long> {

    // 퍼즐 ID로 해당 퍼즐 플레이 정보를 조회
    Optional<PuzzlePlay> findByPuzzle_PuzzleId(Long puzzleId);

    // 퍼즐 UID와 사용자 ID로 해당 퍼즐 플레이 정보를 조회
    @Query(value =
            "SELECT IFNULL(pd.puzzle_uid, '') AS puzzleUid, " +
            "       IFNULL(pd.image_url, '') AS puzzleImageUrl, " +
            "       IFNULL(tpp.puzzle_play_uid, '') AS puzzlePlayUid, " +
            "       IFNULL(tpp.puzzle_play_data, '') AS puzzlePlayData " +
            "FROM (SELECT tp.puzzle_id, tp.puzzle_uid, tis.image_url " +
            "      FROM tb_puzzles tp " +
            "      JOIN tb_image_storage tis ON tp.puzzle_image_id = tis.image_id " +
            "      WHERE tp.puzzle_uid = :puzzleUid) AS pd " +
            "LEFT JOIN tb_puzzle_play tpp " +
            "ON pd.puzzle_id = tpp.puzzle_id AND tpp.user_id = :userId",
            nativeQuery = true
    )
    Map<String, Object> findByPuzzleUidAndUserId(
            @Param("puzzleUid") String puzzleUid,
            @Param("userId") String userId
    );

    // 퍼즐 Play UID로 해당 퍼즐 플레이 정보를 조회
    Optional<PuzzlePlay> findByPuzzlePlayUid(String puzzlePlayUID);

    // Artwork 내 퍼즐 완료 현황 계산 : "완료한 퍼즐 수/총 퍼즐 수" : "1/4", "2/4",,,
    @Query(value = """
        SELECT comp.completedPuzzleCount as completedPuzzleCount, tot.totalPuzzleCount as totalPuzzleCount
          FROM (
            -- Completed Puzzle Count
            SELECT COUNT(tpp.puzzle_play_id) AS completedPuzzleCount
              FROM tb_puzzle_play tpp
             WHERE tpp.puzzle_id = :puzzleId
               AND tpp.user_id = :userId
               AND tpp.is_completed = true
          ) AS comp,
          (
            -- Total Puzzle Count
            SELECT COUNT(tp.puzzle_id) AS totalPuzzleCount
              FROM tb_puzzles tp
             WHERE tp.artwork_id = (
               SELECT tp2.artwork_id
                 FROM tb_puzzles tp2
                WHERE tp2.puzzle_id = :puzzleId
             )
          ) AS tot;
        """, nativeQuery = true)
    Map<String, String> getCompletedPuzzlesFractionByUid(@Param("userId") long userId, @Param("puzzleId") long puzzleId);

    List<PuzzlePlay> findByPuzzle_PuzzleIdAndUser_Id(Long puzzleId, Long userId);

    Optional<PuzzlePlay> findByUserAndPuzzle(User user, Puzzle puzzle);

    // 특정 사용자의 미완료된 퍼즐 조회
    @Query("SELECT pp FROM PuzzlePlay pp JOIN pp.puzzle p JOIN pp.user u " +
            "WHERE u.email = :email AND pp.isCompleted = false")
    List<PuzzlePlay> findPlayingPuzzlesByUserEmail(String email);

    // 특정 퍼즐과 사용자 ID를 기준으로 플레이 기록 존재 여부 확인
    boolean existsByPuzzle_PuzzleIdAndUser_Id(Long puzzleId, Long userId);

}
