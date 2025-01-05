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
@Table(name = "tb_image_storage")
public class ImageStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    @NotNull
    private Long imageId;

    @NotNull
    @Column(name = "image_uid", length = 36, nullable = false)
    private String imageUid;

    @NotNull
    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private ImageType imageType;

    @Column(name = "image_type_id")
    private Long imageTypeId;
}

enum ImageType {
    ARTWORK, PUZZLE
}
