package startoy.puzzletime.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "puzzle_play_id", nullable = false)
    @NotNull
    private Long puzzlePlayId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "puzzle_id", referencedColumnName = "puzzle_id", nullable = false)
    private Puzzle puzzle;

    @NotNull
    @Column(name = "is_completed")
    private Boolean isCompleted;

    @NotNull
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Column(name = "pieces_data", columnDefinition = "json")
    private String piecesData;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
