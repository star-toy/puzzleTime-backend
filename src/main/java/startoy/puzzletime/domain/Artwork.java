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
@Table(name = "tb_artworks")
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artwork_id", nullable = false)
    @NotNull
    private Long artworkId;

    @NotNull
    @Column(name = "artwork_uid", length = 36, nullable = false)
    private String artworkUid;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "theme_id", referencedColumnName = "theme_id", nullable = false)
    private Theme theme;

    @NotNull
    @Column(name = "artwork_title", length = 100, nullable = false)
    private String artworkTitle;

    @NotNull
    @Column(name = "artwork_description", length = 255, nullable = false)
    private String artworkDescription;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artwork_image_id", nullable = false)
    private ImageStorage artworkImage;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "artwork_seq", nullable = false)
    private Integer artworkSeq;

    @NotNull
    @Column(name = "reward_image_id", nullable = false)
    @NotNull
    private Integer rewardImageId;
}
