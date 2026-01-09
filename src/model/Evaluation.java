package model;

import java.io.Serializable; //importe quelquechose pour sauvegarder une chose dans un fichier

public abstract class Evaluation implements Serializable { //compte(mail+mdp) sauvgarde dans un fichier
 private static final long serialVersionUID = 1L; //num id pour la sauvgarde 
 protected String titre;
 protected Matiere matiere;
 protected String corrige;
 protected String cheminFichier; // Chemin du fichier PDF/DOCX

 public Evaluation(String titre, Matiere matiere, String corrige) {
 this.titre = titre;
 this.matiere = matiere;
 this.corrige = corrige;
 this.cheminFichier = null;
 }

 public abstract String getType();

 public String getTitre() { return titre; }
 public Matiere getMatiere() { return matiere; }
 public String getCorrige() { return corrige; }
 public String getCheminFichier() { return cheminFichier; }

 public void setCheminFichier(String chemin) { this.cheminFichier = chemin; }
 public boolean aFichierJoint() { return cheminFichier != null && !cheminFichier.isEmpty(); }

 @Override
 public String toString() {
 String fichier = aFichierJoint() ? " 📎" : "";
 return getType() + " - " + titre + " - " + matiere + fichier;
 }
}