package com.ihec.gestion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
// entité représentant une matière enseignée (ex: Mathématiques, L2, Finance)
@Entity
@Table(name = "matieres")
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @NotBlank
    @Column(nullable = false)
    private String niveau;

    @NotBlank
    @Column(nullable = false)
    private String filiere;

    public Matiere() {}

    public Matiere(String nom, String niveau, String filiere) {
        this.nom = nom;
        this.niveau = niveau;
        this.filiere = filiere;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
}