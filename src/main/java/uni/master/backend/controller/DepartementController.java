package uni.master.backend.controller;

import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uni.master.backend.model.Departement;
import uni.master.backend.model.Employee;
import uni.master.backend.repository.DepartementRepository;
import uni.master.backend.service.DepartementService;

import java.util.List;

@RestController
@RequestMapping("/departements")
public class DepartementController {
    @Autowired private DepartementService departementService;
    @Autowired private DepartementRepository departementRepository;

    @GetMapping
    public List<Departement> getDepartements() {
        return departementService.getAllDepartements();
    }

    @PostMapping
    public boolean addDepartement(@RequestParam(name = "name") String name) {
        Departement departement =new Departement();
        departement.setName(name);
        departement.setEmployees(null);
        departement=departementService.addDepartement(departement);
        return departementRepository.existsById(departement.getId());
    }

}
