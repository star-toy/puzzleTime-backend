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
@Table(name = "tb_artworks")
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artwork_id")
    private Long artworkId;

    @Column(name = "artwork_uid", length = 36)
    private String artworkUid;

    @Column(name = "artwork_title", length = 100)
    private String artworkTitle;

    @Column(name = "artwork_description", length = 255)
    private String artworkDescription;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "total_stages", nullable = false)
    private int totalStages;
}
