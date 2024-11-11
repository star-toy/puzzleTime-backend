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

}