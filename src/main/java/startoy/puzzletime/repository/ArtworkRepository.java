package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startoy.puzzletime.domain.Artwork;

import java.util.List;
import java.util.Optional;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    // UID 로 Artwork 조회
    Optional<Artwork> findByArtworkUid(String artworkUid);

    // 특정 ThemeId로 Artwork 리스트 조회
    List<Artwork> findByThemeThemeId(Long themeId);

}
