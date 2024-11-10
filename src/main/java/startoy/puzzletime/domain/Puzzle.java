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
@Table(name = "tb_puzzles")
public class Puzzle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "puzzle_id")
    private Long puzzleId;

    @Column(name = "puzzle_uid", length = 36, nullable = false)
    private String puzzleUid;

    @ManyToOne
    @JoinColumn(name = "artwork_id", referencedColumnName = "artwork_id", nullable = false)
    private Artwork artwork;

    @Column(name = "puzzle_index", nullable = false)
    private int puzzleIndex;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

