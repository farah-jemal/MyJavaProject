package com.ihec.gestion.service;

import com.ihec.gestion.model.Matiere;
import com.ihec.gestion.repository.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatiereService {

    @Autowired
    private MatiereRepository matiereRepository;

    public Matiere sauvegarder(Matiere matiere) { return matiereRepository.save(matiere); }

    public List<Matiere> getToutesLesMatieres() { return matiereRepository.findAll(); }

    public Optional<Matiere> getMatiereParId(Long id) { return matiereRepository.findById(id); }

    public List<Matiere> filtrer(String filiere, String niveau) {
        if (!filiere.isEmpty() && !niveau.isEmpty())
            return matiereRepository.findByFiliereContainingIgnoreCaseAndNiveauContainingIgnoreCase(filiere, niveau);
        if (!filiere.isEmpty())
            return matiereRepository.findByFiliereContainingIgnoreCase(filiere);
        if (!niveau.isEmpty())
            return matiereRepository.findByNiveauContainingIgnoreCase(niveau);
        return matiereRepository.findAll();
    }

    public boolean supprimer(Long id) {
        if (!matiereRepository.existsById(id)) return false;
        matiereRepository.deleteById(id);
        return true;
    }

    public Optional<Matiere> mettreAJour(Long id, Matiere donnees) {
        return matiereRepository.findById(id).map(m -> {
            m.setNom(donnees.getNom());
            m.setNiveau(donnees.getNiveau());
            m.setFiliere(donnees.getFiliere());
            return matiereRepository.save(m);
        });
    }
}