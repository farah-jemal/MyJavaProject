package com.ihec.gestion.controller;

import com.ihec.gestion.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private EvaluationService evaluationService;
    // page d'accueil : on affiche juste le nombre d'évaluations disponibles
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("nbEvaluations", evaluationService.getNombreEvaluations());
        return "index";
    }
}