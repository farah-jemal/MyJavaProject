package model;

import java.io.Serializable;

public class Matiere implements Serializable {
 private static final long serialVersionUID = 1L;
 private String nom;
 private String niveau;
 private String filiere;

 public Matiere(String nom, String niveau, String filiere) {
 this.nom = nom;
 this.niveau = niveau;
 this.filiere = filiere;
 }

 public String getNom() { return nom; }
 public String getNiveau() { return niveau; }
 public String getFiliere() { return filiere; }

 @Override
 public String toString() {
 return nom + " - " + filiere + " - " + niveau;
 }
}