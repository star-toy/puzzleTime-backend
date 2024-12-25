package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startoy.puzzletime.domain.UserArtwork;

public interface UserArtworkRepository extends JpaRepository<UserArtwork, Long> {
}
