package com.ihec.gestion.controller;

import com.ihec.gestion.model.Compte;
import com.ihec.gestion.model.TypeUtilisateur;
import com.ihec.gestion.service.CompteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comptes")
public class CompteController {

    @Autowired
    private CompteService compteService;

    //page de connexion, on récupère le type passé(prof ou etudiant)
    //si rien n est precie on me etudiant par defaut
    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String type, Model model) {
        model.addAttribute("typeParam", type != null ? type : "ETUDIANT");
        model.addAttribute("typeLabel", "PROF".equals(type) ? "Professeur" : "Etudiant");
        return "login";
    }

    // Traitement du formulaire de  connexion
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email,
                              @RequestParam String motDePasse,
                              @RequestParam(required = false) String type,
                              HttpSession session,
                              RedirectAttributes redirectAttrs) {
        // on refuse tout email qui n est pas @ihec.tn
        if (email == null || !email.toLowerCase().endsWith("@ihec.tn")) {
            redirectAttrs.addFlashAttribute("error", "Seules les adresses @ihec.tn sont autorisées !");
            return "redirect:/comptes/login?type=" + (type != null ? type : "ETUDIANT");
        }
        TypeUtilisateur typeEnum = "PROF".equals(type) ? TypeUtilisateur.PROF : TypeUtilisateur.ETUDIANT;
        Compte compte = compteService.authentifier(email, motDePasse, typeEnum);
        if (compte != null) {
            session.setAttribute("user", compte);
            return "redirect:/dashboard";
        }
        redirectAttrs.addFlashAttribute("error", "Email ou mot de passe incorrect");
        return "redirect:/comptes/login?type=" + (type != null ? type : "ETUDIANT");
    }

    // Déconnexion : on vide la session et on renvoie a la page d accueil
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // fomulaire d'inscription
    @GetMapping("/inscrire")
    public String inscrireForm() {
        return "inscrire";
    }

    // Traitement  d'inscription
    @PostMapping("/inscrire")
    public String inscrireSubmit(@RequestParam String email,
                                 @RequestParam String motDePasse,
                                 @RequestParam TypeUtilisateur type,
                                 RedirectAttributes redirectAttrs) {
        // même vérification que pour le login, l'email doit être @ihec.tn
        if (email == null || !email.toLowerCase().endsWith("@ihec.tn")) {
            redirectAttrs.addFlashAttribute("error", "Seules les adresses @ihec.tn sont autorisées !");
            return "redirect:/comptes/inscrire";
        }
        boolean succes = compteService.inscrire(email, motDePasse, type);
        if (succes) {
            redirectAttrs.addFlashAttribute("success", "Compte créé avec succès !");
            return "redirect:/comptes/login";
        }
        //l email existe deja en BD
        redirectAttrs.addFlashAttribute("error", "Email déjà utilisé");
        return "redirect:/comptes/inscrire";
    }
}