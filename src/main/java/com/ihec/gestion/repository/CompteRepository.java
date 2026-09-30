package com.ihec.gestion.repository;

import com.ihec.gestion.model.Compte;
import com.ihec.gestion.model.TypeUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    // utilisé pour l'authentification : les 3 critères doivent correspondre
    Optional<Compte> findByEmailAndMotDePasseAndType(String email, String motDePasse, TypeUtilisateur type);
    // vérification rapide avant inscription pour éviter les doublons
    boolean existsByEmail(String email);
    Optional<Compte> findByEmail(String email);
}