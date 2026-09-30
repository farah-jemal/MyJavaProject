package com.ihec.gestion.repository;

import com.ihec.gestion.model.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    // recherches insensibles à la casse pour le filtrage côté service
    List<Matiere> findByFiliereContainingIgnoreCase(String filiere);
    List<Matiere> findByNiveauContainingIgnoreCase(String niveau);
    List<Matiere> findByFiliereContainingIgnoreCaseAndNiveauContainingIgnoreCase(String filiere, String niveau);
}