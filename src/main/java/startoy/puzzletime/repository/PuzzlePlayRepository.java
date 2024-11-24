package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import startoy.puzzletime.domain.Puzzle;
import startoy.puzzletime.domain.PuzzlePlay;
import startoy.puzzletime.domain.User;
import startoy.puzzletime.dto.puzzle.GetPuzzleResponse;

import java.util.Map;
import java.util.Optional;

@Repository
@SqlResultSetMapping(
    name = "GetPuzzleResponseMapping",
    classes = @ConstructorResult(
        targetClass = GetPuzzleResponse.class,
        columns = {
            @ColumnResult(name = "puzzleUid", type = String.class),
            @ColumnResult(name = "imageUrl", type = String.class),
            @ColumnResult(name = "puzzlePlayUid", type = String.class),
            @ColumnResult(name = "userId", type = String.class),
            @ColumnResult(name = "isCompleted", type = Boolean.class),
            @ColumnResult(name = "piecesCount", type = Integer.class),
            @ColumnResult(name = "piecesData", type = String.class)
        }
    )
)
public interface PuzzlePlayRepository extends JpaRepository<PuzzlePlay, Long> {

    // 퍼즐 ID로 해당 퍼즐 플레이 정보를 조회
    Optional<PuzzlePlay> findByPuzzle_PuzzleId(Long puzzleId);

    // 퍼즐 UID와 사용자 ID로 해당 퍼즐 플레이 정보를 조회
    @Query(value =
            "SELECT pd.puzzle_uid AS puzzleUid, pd.image_url AS imageUrl, " +
                    "       tpp.puzzle_play_uid AS puzzlePlayUid, tpp.user_id AS userId, " +
                    "       tpp.is_completed AS isCompleted, tpp.pieces_count AS piecesCount, tpp.pieces_data AS piecesData " +
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
    /* #region : native query findByPuzzleUidAndUserId
     *    SELECT pd.puzzle_uid, pd.image_url, tpp.puzzle_play_uid, tpp.user_id, tpp.is_completed, tpp.pieces_count, tpp.pieces_data
     *    from (select tp.puzzle_id, tp.puzzle_uid, tis.image_url
     *                    from tb_puzzles tp
     *                  join tb_image_storage tis
     *                  on tp.puzzle_image_id = tis.image_id
     *                  where tp.puzzle_uid = :puzzleUid	-- tb_puzzles 중 일치하는 퍼즐 정보 조회
     *            ) as pd -- puzzle data
     *    left join tb_puzzle_play tpp
     *    on pd.puzzle_id = tpp.puzzle_id
     *    and tpp.user_id = :userId				-- tb_puzzle_play 중 일치하는 사용자 정보 조회
     * #endregion */


    Optional<PuzzlePlay> findByPuzzle_PuzzleIdAndUser_Id(Long puzzleId, Long userId);

    Optional<PuzzlePlay> findByUserAndPuzzle(User user, Puzzle puzzle);

}
