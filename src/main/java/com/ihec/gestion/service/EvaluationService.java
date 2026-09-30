package com.ihec.gestion.service;

import com.ihec.gestion.model.*;
import com.ihec.gestion.repository.EvaluationRepository;
import com.ihec.gestion.repository.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Value("${app.corriges.dossier:corriges}")
    private String dossierCorriges;

    public Evaluation ajouterEvaluation(String titre, Long matiereId, String typeEval) {
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new RuntimeException("Matière introuvable : " + matiereId));

        Evaluation evaluation;
        if ("DS".equalsIgnoreCase(typeEval)) {
            evaluation = new DS(titre, matiere);
        } else if ("Examen".equalsIgnoreCase(typeEval)) {
            evaluation = new Examen(titre, matiere);
        } else {
            throw new RuntimeException("Type invalide : " + typeEval);
        }
        return evaluationRepository.save(evaluation);
    }

    public List<Evaluation> getToutesLesEvaluations() { return evaluationRepository.findAll(); }

    public Optional<Evaluation> getEvaluationParId(Long id) { return evaluationRepository.findById(id); }

    public List<Evaluation> filtrer(String filiere, String niveau, String matiere) {
        return evaluationRepository.filtrer(
                filiere  == null ? "" : filiere,
                niveau   == null ? "" : niveau,
                matiere  == null ? "" : matiere);
    }

    public Optional<Evaluation> mettreAJour(Long id, String nouveauTitre, Long nouvelleMatiereId) {
        return evaluationRepository.findById(id).map(eval -> {
            eval.setTitre(nouveauTitre);
            if (nouvelleMatiereId != null)
                matiereRepository.findById(nouvelleMatiereId).ifPresent(eval::setMatiere);
            return evaluationRepository.save(eval);
        });
    }

    public boolean supprimerEvaluation(Long id) {
        Optional<Evaluation> opt = evaluationRepository.findById(id);
        if (opt.isEmpty()) return false;
        Evaluation eval = opt.get();
        if (eval.aFichierJoint()) {
            try { Files.deleteIfExists(Paths.get(eval.getCheminFichier())); }
            catch (IOException e) { System.err.println("Erreur suppression fichier : " + e.getMessage()); }
        }
        evaluationRepository.deleteById(id);
        return true;
    }

    public Evaluation uploadFichier(Long evaluationId, MultipartFile fichier) throws IOException {
        Evaluation eval = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Évaluation introuvable : " + evaluationId));

        new File(dossierCorriges).mkdirs();

        String nomOriginal = fichier.getOriginalFilename();
        String extension = (nomOriginal != null && nomOriginal.contains("."))
                ? nomOriginal.substring(nomOriginal.lastIndexOf(".")) : "";
        String nomFichier = eval.getTitre().replaceAll("[^a-zA-Z0-9]", "_")
                + "_" + System.currentTimeMillis() + extension;

        Path destination = Paths.get(dossierCorriges, nomFichier);
        Files.copy(fichier.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        if (eval.aFichierJoint())
            Files.deleteIfExists(Paths.get(eval.getCheminFichier()));

        eval.setCheminFichier(destination.toString());
        return evaluationRepository.save(eval);
    }

    public long getNombreEvaluations() { return evaluationRepository.count(); }

    public Evaluation setCorrection(Long id, String correction) {
        return evaluationRepository.findById(id).map(eval -> {
            eval.setCorrection(correction);
            return evaluationRepository.save(eval);
        }).orElseThrow();
    }
}