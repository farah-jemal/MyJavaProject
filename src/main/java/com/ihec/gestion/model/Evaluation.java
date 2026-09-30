package com.ihec.gestion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// classe abstraite commune à DS et Examen
// on utilise SINGLE_TABLE pour stocker les deux types dans la même table
// avec une colonne discriminante "type_eval"
@Entity
@Table(name = "evaluations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_eval", discriminatorType = DiscriminatorType.STRING)
public abstract class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    protected String titre;

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    @NotNull
    protected Matiere matiere;

    // chemin vers le fichier PDF/DOCX uploadé, peut être null si pas de fichier joint
    @Column(name = "chemin_fichier")
    protected String cheminFichier;

    // chaque sous-classe retourne son propre type ("DS" ou "Examen")
    public abstract String getType();

    public Evaluation() {}

    public Evaluation(String titre, Matiere matiere) {
        this.titre = titre;
        this.matiere = matiere;
    }
    // petite méthode utilitaire pour savoir si un fichier est joint
    public boolean aFichierJoint() {
        return cheminFichier != null && !cheminFichier.isEmpty();
    }
    // le corrigé textuel, stocké en TEXT pour pouvoir être long
    @Column(columnDefinition = "TEXT")
    private String correction;

    public String getCorrection() { return correction; }
    public void setCorrection(String correction) { this.correction = correction; }

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String c) { this.cheminFichier = c; }
}