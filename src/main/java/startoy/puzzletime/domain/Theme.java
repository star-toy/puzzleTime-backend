package startoy.puzzletime.domain;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "theme_id")
    private Long themeId;

    @Column(name = "theme_uid", length = 36)
    private String themeUid;

    @Column(name = "theme_title", length = 100)
    private String themeTitle;

    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "bgm_id")
    private Long bgmId;
}
