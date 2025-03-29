package uni.master.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.master.backend.model.Departement;

public interface DepartementRepository extends JpaRepository<Departement, Long> {
}