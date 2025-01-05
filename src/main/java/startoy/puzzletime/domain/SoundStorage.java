package startoy.puzzletime.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_sound_storage")
public class SoundStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sound_id",nullable = false)
    @NotNull
    private Long soundId;

    @NotNull
    @Column(name = "sound_title", length = 255, nullable = false)
    private String soundTitle;

    @NotNull
    @Column(name = "sound_url", length = 255, nullable = false)
    private String soundUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sound_type", nullable = false)
    private SoundType soundType;

    @NotNull
    @Column(name = "sound_source", length = 255, nullable = false)
    private String soundSource;
}

enum SoundType {
    BGM, EFFECT
}
