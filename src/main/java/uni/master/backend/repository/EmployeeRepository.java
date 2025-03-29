package uni.master.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.master.backend.model.Departement;
import uni.master.backend.model.Employee;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByBirthDateBefore(LocalDate date);
}
