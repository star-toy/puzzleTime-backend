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
@Table(name = "tb_puzzles")
public class Puzzle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "puzzle_id", nullable = false)
    @NotNull
    private Long puzzleId;

    @NotNull
    @Column(name = "puzzle_uid", length = 36, nullable = false)
    private String puzzleUid;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artwork_id", referencedColumnName = "artwork_id", nullable = false)
    private Artwork artwork;

    @NotNull
    @Column(name = "puzzle_index", nullable = false)
    private int puzzleIndex;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "puzzle_image_id", nullable = false)
    private ImageStorage puzzleImage;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

