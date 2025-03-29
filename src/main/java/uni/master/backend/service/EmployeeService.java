package uni.master.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uni.master.backend.model.Employee;
import uni.master.backend.repository.EmployeeRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;


@Service
public class EmployeeService {
    private final Path root = Paths.get("src/main/resources/static/photos"); // Répertoire de stockage

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Sauvegarder la photo
    public String savePhoto(Long id,MultipartFile photo) throws IOException {
        if (!Files.exists(root)) Files.createDirectories(root);
        String filename = id+".png";
        Files.copy(photo.getInputStream(), root.resolve(filename));
        return "http://localhost:8080/employes/photos/"+filename;
    }

    // Charger la photo
    public Path loadPhoto(String filename) {
        return root.resolve(filename);
    }

    // Sauvegarder l'employé
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);

            // Delete the associated photo if it exists
            Path photoPath = root.resolve(id + ".png");
            try {
                Files.deleteIfExists(photoPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete photo for employee ID: " + id, e);
            }

            return true;
        }
        return false;
    }

    public int deleteEmployeesOlderThan(int age) {
        LocalDate thresholdDate = LocalDate.now().minusYears(age);
        List<Employee> employeesToDelete = employeeRepository.findByBirthDateBefore(thresholdDate);

        if (!employeesToDelete.isEmpty()) {
            employeeRepository.deleteAll(employeesToDelete);
            return employeesToDelete.size();
        }

        return 0;
    }
}