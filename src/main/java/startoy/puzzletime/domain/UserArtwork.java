package startoy.puzzletime.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user_artworks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserArtwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_artwork_id")
    private Long userArtworkId;

    @Column(name = "user_artwork_uid", length = 36, nullable = false, unique = true)
    private String userArtworkUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id", referencedColumnName = "artwork_id", nullable = false)
    private Artwork artwork;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
