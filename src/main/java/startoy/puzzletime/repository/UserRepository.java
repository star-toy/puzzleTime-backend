package startoy.puzzletime.repository;

//사용자 정보를 데이터베이스에서 관리할 리포지토리.

import startoy.puzzletime.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderId(String providerId);
}