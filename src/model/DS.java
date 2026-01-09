package model;

public class DS extends Evaluation {
 private static final long serialVersionUID = 1L;

 public DS(String titre, Matiere matiere, String corrige) {
 super(titre, matiere, corrige);
 }
 //  méthode dit quel est le type d'évaluation
 @Override
 public String getType() { return "DS"; }
}