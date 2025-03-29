package uni.master.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uni.master.backend.model.Departement;
import uni.master.backend.model.Employee;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uni.master.backend.repository.DepartementRepository;
import uni.master.backend.repository.EmployeeRepository;
import uni.master.backend.service.EmployeeService;


import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/employes")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    // Endpoint pour ajouter un employé avec photo
    @PostMapping
    public ResponseEntity<String> addEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String birthDate,
            @RequestParam Long departmentId,
            @RequestParam MultipartFile photo
    ) {
        try {
            // Trouver le département
            Departement department = departementRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Département non trouvé"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String filename="http://localhost:8080/employes/photos/0.png";
            // Créer l'employé
            Employee employee = new Employee();
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setBirthDate(LocalDate.parse(birthDate, formatter));
            employee.setDepartement(department);
            employee.setPhotoPath(filename);

            employee=employeeService.saveEmployee(employee);
            // Sauvegarder la photo et obtenir le nom du fichier
            employee = employeeRepository.findById(employee.getId()).get();
            filename = employeeService.savePhoto(employee.getId(),photo);

            employee.setPhotoPath(filename);
            employeeService.saveEmployee(employee);

            return ResponseEntity.ok("Employé ajouté");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors de l'upload de la photo");
        }
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Endpoint pour récupérer une photo
    @GetMapping("/photos/{id}")
    public ResponseEntity<Resource> getPhoto(@PathVariable Long id) throws IOException {
        Path filePath = employeeService.loadPhoto(String.valueOf(id));
        Resource resource = new FileSystemResource(filePath.toFile());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.deleteEmployee(id);

        if (deleted) {
            return ResponseEntity.ok("Employee deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
        }
    }

    @DeleteMapping("/age/{age}")
    public ResponseEntity<String> deleteEmployeesByAge(@PathVariable int age) {
        int deletedCount = employeeService.deleteEmployeesOlderThan(age);

        if (deletedCount > 0) {
            return ResponseEntity.ok(deletedCount + " employees deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employees older than " + age + " found.");
        }
    }

}