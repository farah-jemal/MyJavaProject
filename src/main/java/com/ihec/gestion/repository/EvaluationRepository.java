package com.ihec.gestion.repository;

import com.ihec.gestion.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    // recherche combinée par filière, niveau et nom de matière
    // si un critère est vide on ignore le filtre (c'est le LIKE '%' qui fait ça)
    @Query("SELECT e FROM Evaluation e WHERE " +
            "(:filiere = '' OR LOWER(e.matiere.filiere) LIKE LOWER(CONCAT('%', :filiere, '%'))) AND " +
            "(:niveau  = '' OR LOWER(e.matiere.niveau)  LIKE LOWER(CONCAT('%', :niveau,  '%'))) AND " +
            "(:matiere = '' OR LOWER(e.matiere.nom)     LIKE LOWER(CONCAT('%', :matiere, '%')))")
    List<Evaluation> filtrer(@Param("filiere") String filiere,
                             @Param("niveau")  String niveau,
                             @Param("matiere") String matiere);

    List<Evaluation> findByMatiereId(Long matiereId);
}