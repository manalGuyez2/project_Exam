package uni.master.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.master.backend.model.Departement;
import uni.master.backend.repository.DepartementRepository;

import java.util.List;

@Service
public class DepartementService {

    @Autowired private DepartementRepository departementRepository;

    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }
    public Departement getDepartementById(Long id) {
        return departementRepository.findById(id).orElse(null);
    }
    public Departement addDepartement(Departement departement) {
        return departementRepository.save(departement);
    }
    public boolean deleteDepartementById(Long id) {
        departementRepository.deleteById(id);
        return !departementRepository.existsById(id);
    }
}
