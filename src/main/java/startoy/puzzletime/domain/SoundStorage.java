package startoy.puzzletime.domain;

import jakarta.persistence.*;
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
    @Column(name = "sound_id")
    private Long soundId;

    @Column(name = "sound_title", length = 255)
    private String soundTitle;

    @Column(name = "sound_url", length = 255)
    private String soundUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "sound_type")
    private SoundType soundType;

    @Column(name = "sound_source", length = 255)
    private String soundSource;
}

enum SoundType {
    BGM, EFFECT
}
