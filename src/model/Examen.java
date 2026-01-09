package model;

public class Examen extends Evaluation {
 private static final long serialVersionUID = 1L;

 public Examen(String titre, Matiere matiere, String corrige) {
 super(titre, matiere, corrige);
 }

 @Override
 public String getType() { return "Examen"; }
}