package model;

public class Utilisateur {
 public enum Type { ETUDIANT, PROF } //enum liste fermé de choix possibles .le  type ne peux avoir que etudiant ou prof 

 private String nom;
 private Type type;

 public Utilisateur(String nom, Type type) {
 this.nom = nom;
 this.type = type;
 }

 public String getNom() { return nom; }
 public Type getType() { return type; }
}