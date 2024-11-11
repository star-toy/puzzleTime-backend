package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startoy.puzzletime.domain.Artwork;

import java.util.Optional;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    Optional<Artwork> findByArtworkUid(String artworkUid); // UID 로 Artwork 조회

}
