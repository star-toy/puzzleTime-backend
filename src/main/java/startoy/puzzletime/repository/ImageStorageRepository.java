package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startoy.puzzletime.domain.ImageStorage;
import java.util.Optional;

@Repository
public interface ImageStorageRepository extends JpaRepository<ImageStorage, Long> {
    // imageId로 ImageStorage 조회
    Optional<ImageStorage> findById(Long imageId);

}