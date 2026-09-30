package com.ihec.gestion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "comptes")
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    // le mot de passe est stocké en clair pour l'instant (pas de hachage dans ce projet)
    @NotBlank
    @Column(nullable = false)
    private String motDePasse;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeUtilisateur type;

    public Compte() {}

    public Compte(String email, String motDePasse, TypeUtilisateur type) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public TypeUtilisateur getType() { return type; }
    public void setType(TypeUtilisateur type) { this.type = type; }
}