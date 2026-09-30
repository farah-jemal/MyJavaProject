package com.ihec.gestion.controller;

import com.ihec.gestion.model.Compte;
import com.ihec.gestion.model.Matiere;
import com.ihec.gestion.model.TypeUtilisateur;
import com.ihec.gestion.service.EvaluationService;
import com.ihec.gestion.service.MatiereService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private MatiereService matiereService;
    //dossier ou sont stockes les fichiers corriges(definies dans application properties
    @Value("${app.corriges.dossier:corriges}")
    private String dossierCorriges;

    // vue etudiant : lecture seule, il peut just consulter
    @GetMapping("/etudiant")
    public String etudiant(HttpSession session, Model model) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null) return "redirect:/comptes/login";
        // si c'est un prof qui arrive ici par erreur, on le redirige vers sa vraie page
        if (compte.getType() == TypeUtilisateur.PROF) return "redirect:/evaluations/prof";
        model.addAttribute("user", compte);
        model.addAttribute("evaluations", evaluationService.getToutesLesEvaluations());
        return "evaluations-etudiant";
    }

    // vue prof : il peut tout faire (ajouter, modifier, supprimer)
    @GetMapping("/prof")
    public String prof(HttpSession session, Model model) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null) return "redirect:/comptes/login";
        if (compte.getType() != TypeUtilisateur.PROF) return "redirect:/evaluations/etudiant";
        model.addAttribute("user", compte);
        model.addAttribute("evaluations", evaluationService.getToutesLesEvaluations());
        model.addAttribute("matieres", matiereService.getToutesLesMatieres());
        return "evaluations-prof";
    }

    // ajout d'une nouvelle évaluation
    @PostMapping("/ajouter")
    public String ajouter(HttpSession session,
                          @RequestParam String titre,
                          @RequestParam(required = false) Long matiereId,
                          @RequestParam(required = false) String matiereNom,
                          @RequestParam(required = false) String matiereNiveau,
                          @RequestParam(required = false) String matiereFiliere,
                          @RequestParam String typeEval,
                          @RequestParam(required = false) String correction,
                          @RequestParam(required = false) MultipartFile fichier,
                          RedirectAttributes redirectAttrs) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null || compte.getType() != TypeUtilisateur.PROF)
            return "redirect:/comptes/login";
        try {
            // si le prof n'a pas choisi une matière existante, on en crée une nouvelle à la volée
            if (matiereId == null) {
                if (matiereNom == null || matiereNom.isBlank()) {
                    redirectAttrs.addFlashAttribute("error", "Veuillez saisir une matière !");
                    return "redirect:/evaluations/prof";
                }
                Matiere nouvelle = matiereService.sauvegarder(new Matiere(
                        matiereNom,
                        matiereNiveau != null && !matiereNiveau.isBlank() ? matiereNiveau : "-",
                        matiereFiliere != null && !matiereFiliere.isBlank() ? matiereFiliere : "-"
                ));
                matiereId = nouvelle.getId();
            }
            var eval = evaluationService.ajouterEvaluation(titre, matiereId, typeEval);
            // on sauvegarde la correction texte si elle est renseignée
            if (correction != null && !correction.isBlank())
                evaluationService.setCorrection(eval.getId(), correction);
            // pareil pour le fichier joint
            if (fichier != null && !fichier.isEmpty())
                evaluationService.uploadFichier(eval.getId(), fichier);
            redirectAttrs.addFlashAttribute("success", "Évaluation ajoutée !");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/evaluations/prof";
    }

    // suppression d'une évaluation (réservé au prof)
    @GetMapping("/supprimer/{id}")
    public String supprimer(HttpSession session, @PathVariable Long id,
                            RedirectAttributes redirectAttrs) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null || compte.getType() != TypeUtilisateur.PROF)
            return "redirect:/comptes/login";
        evaluationService.supprimerEvaluation(id);
        redirectAttrs.addFlashAttribute("success", "Supprimée !");
        return "redirect:/evaluations/prof";
    }

    // téléchargement du fichier joint, accessible à tout utilisateur connecté
    @GetMapping("/fichier/{id}")
    public ResponseEntity<Resource> telecharger(HttpSession session,
                                                @PathVariable Long id) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null) return ResponseEntity.status(401).build();
        try {
            var eval = evaluationService.getEvaluationParId(id).orElseThrow();
            if (!eval.aFichierJoint()) return ResponseEntity.notFound().build();
            Path chemin = Paths.get(eval.getCheminFichier());
            Resource resource = new UrlResource(chemin.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + chemin.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // retourne les données d'une éval en JSON pour préremplir le formulaire de modification
    @GetMapping("/modifier/{id}")
    @ResponseBody
    public ResponseEntity<?> getEvalData(HttpSession session, @PathVariable Long id) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null || compte.getType() != TypeUtilisateur.PROF)
            return ResponseEntity.status(403).build();
        return evaluationService.getEvaluationParId(id)
                .map(eval -> {
                    java.util.Map<String, Object> data = new java.util.HashMap<>();
                    data.put("id", eval.getId());
                    data.put("titre", eval.getTitre());
                    data.put("type", eval.getType());
                    data.put("correction", eval.getCorrection());
                    data.put("matiereId", eval.getMatiere().getId());
                    data.put("matiereNom", eval.getMatiere().getNom());
                    data.put("aFichier", eval.aFichierJoint());
                    return ResponseEntity.ok(data);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // sauvegarde des modifications d'une évaluation existante
    @PostMapping("/modifier/{id}")
    public String modifier(HttpSession session,
                           @PathVariable Long id,
                           @RequestParam String titre,
                           @RequestParam(required = false) Long matiereId,
                           @RequestParam(required = false) String matiereNom,
                           @RequestParam(required = false) String matiereNiveau,
                           @RequestParam(required = false) String matiereFiliere,
                           @RequestParam(required = false) String correction,
                           @RequestParam(required = false) MultipartFile fichier,
                           RedirectAttributes redirectAttrs) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null || compte.getType() != TypeUtilisateur.PROF)
            return "redirect:/comptes/login";
        try {
            if (matiereId == null) {
                if (matiereNom == null || matiereNom.isBlank()) {
                    redirectAttrs.addFlashAttribute("error", "Veuillez saisir une matière !");
                    return "redirect:/evaluations/prof";
                }
                Matiere nouvelle = matiereService.sauvegarder(new Matiere(
                        matiereNom,
                        matiereNiveau != null && !matiereNiveau.isBlank() ? matiereNiveau : "-",
                        matiereFiliere != null && !matiereFiliere.isBlank() ? matiereFiliere : "-"
                ));
                matiereId = nouvelle.getId();
            }
            evaluationService.mettreAJour(id, titre, matiereId);
            if (correction != null && !correction.isBlank())
                evaluationService.setCorrection(id, correction);
            if (fichier != null && !fichier.isEmpty())
                evaluationService.uploadFichier(id, fichier);
            redirectAttrs.addFlashAttribute("success", "Évaluation modifiée !");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/evaluations/prof";
    }
}