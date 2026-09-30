package com.ihec.gestion;

import com.ihec.gestion.model.Compte;
import com.ihec.gestion.model.TypeUtilisateur;
import com.ihec.gestion.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
// s'exécute au démarrage de l'application pour insérer des données de test
// on vérifie qu'il n'y a rien en base avant d'insérer, pour ne pas dupliquer à chaque restart
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CompteRepository compteRepository;

    @Override
    public void run(String... args) {
        if (compteRepository.count() == 0) {
            compteRepository.save(new Compte("etudiant@ihec.tn", "etudiant123", TypeUtilisateur.ETUDIANT));
            compteRepository.save(new Compte("prof@ihec.tn", "prof123", TypeUtilisateur.PROF));
            System.out.println("Comptes par défaut créés");
        }
        System.out.println(" Application disponible sur http://localhost:8081");
    }
}