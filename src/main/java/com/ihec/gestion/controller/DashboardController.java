package com.ihec.gestion.controller;

import com.ihec.gestion.model.Compte;
import com.ihec.gestion.model.TypeUtilisateur;
import com.ihec.gestion.service.EvaluationService;
import com.ihec.gestion.service.MatiereService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private MatiereService matiereService;

    @Autowired
    private EvaluationService evaluationService;
    //point d entree apres connexion : on redirige vers la bonne vue selon le role
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Compte compte = (Compte) session.getAttribute("user");
        if (compte == null) return "redirect:/comptes/login";
        model.addAttribute("user", compte);
        model.addAttribute("evaluations", evaluationService.getToutesLesEvaluations());
        model.addAttribute("matieres", matiereService.getToutesLesMatieres());
        //prof->page de gestion , etudiant -> page de consultation
        if (compte.getType() == TypeUtilisateur.PROF) return "evaluations-prof";
        else return "evaluations-etudiant";
    }
}