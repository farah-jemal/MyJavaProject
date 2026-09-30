package com.ihec.gestion.service;

import com.ihec.gestion.model.Compte;
import com.ihec.gestion.model.TypeUtilisateur;
import com.ihec.gestion.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompteService {

    @Autowired
    private CompteRepository compteRepository;

    public Compte authentifier(String email, String motDePasse, TypeUtilisateur type) {
        return compteRepository.findByEmailAndMotDePasseAndType(email, motDePasse, type).orElse(null);
    }

    public boolean inscrire(String email, String motDePasse, TypeUtilisateur type) {
        if (compteRepository.existsByEmail(email)) return false;
        compteRepository.save(new Compte(email, motDePasse, type));
        return true;
    }

    public List<Compte> getTousLesComptes() { return compteRepository.findAll(); }

    public boolean supprimerCompte(Long id) {
        if (!compteRepository.existsById(id)) return false;
        compteRepository.deleteById(id);
        return true;
    }
}