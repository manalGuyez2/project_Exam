package uni.master.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uni.master.backend.model.UserEntity;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
