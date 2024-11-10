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
@Table(name = "tb_theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id", nullable = false)
    @NotNull
    private Long themeId;

    @NotNull
    @Column(name = "theme_uid", length = 36, nullable = false)
    private String themeUid;

    @NotNull
    @Column(name = "theme_title", length = 100, nullable = false)
    private String themeTitle;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private ImageStorage image;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "bgm_id", nullable = false)
    private SoundStorage bgm;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
