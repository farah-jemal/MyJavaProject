package com.ihec.gestion.controller;

import com.ihec.gestion.model.Compte;
import com.ihec.gestion.model.Matiere;
import com.ihec.gestion.model.TypeUtilisateur;
import com.ihec.gestion.service.MatiereService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/matieres")
public class MatiereController {

    @Autowired
    private MatiereService matiereService;

    // liste toutes les matières, la vue affichée dépend du rôle
    @GetMapping
    public String lister(HttpSession session, Model model) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null) return "redirect:/comptes/login";
        model.addAttribute("user", compte);
        model.addAttribute("matieres", matiereService.getToutesLesMatieres());
        if (compte.getType() == TypeUtilisateur.PROF) return "matieres-prof";
        else return "matieres-etudiant";
    }
    // ajout d'une matière, réservé au prof
    @PostMapping("/ajouter")
    public String ajouter(HttpSession session,
                          @RequestParam String nom,
                          @RequestParam String niveau,
                          @RequestParam String filiere,
                          RedirectAttributes redirectAttrs) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null || compte.getType() != TypeUtilisateur.PROF)
            return "redirect:/comptes/login";
        matiereService.sauvegarder(new Matiere(nom, niveau, filiere));
        redirectAttrs.addFlashAttribute("success", "Matière ajoutée !");
        return "redirect:/matieres";
    }
    // suppression d'une matière par son id
    @GetMapping("/supprimer/{id}")
    public String supprimer(HttpSession session, @PathVariable Long id,
                            RedirectAttributes redirectAttrs) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null || compte.getType() != TypeUtilisateur.PROF)
            return "redirect:/comptes/login";
        matiereService.supprimer(id);
        redirectAttrs.addFlashAttribute("success", "Matière supprimée !");
        return "redirect:/matieres";
    }
}