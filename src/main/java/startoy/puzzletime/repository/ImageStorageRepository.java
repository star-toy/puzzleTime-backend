package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startoy.puzzletime.domain.ImageStorage;
@Repository
public interface ImageStorageRepository extends JpaRepository<ImageStorage, Long> {
    // 추가 메서드가 필요하지 않지만, 필요시 정의 가능

}