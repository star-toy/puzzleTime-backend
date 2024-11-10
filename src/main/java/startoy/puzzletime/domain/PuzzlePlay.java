package startoy.puzzletime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_puzzle_play")
public class PuzzlePlay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "puzzle_play_id")
    private Long puzzlePlayId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "puzzle_id", referencedColumnName = "puzzle_id", nullable = false)
    private Puzzle puzzle;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "user_stage_pref_id")
    private Long userStagePrefId;

    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "pieces_count")
    private int piecesCount;

    @Column(name = "pieces_data")
    private String piecesData;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
