package service; 

import java.io.*;                    // Pour lire/écrire des fichiers
import java.nio.file.*;              // Pour copier des fichiers
import java.util.ArrayList;          // Pour  listes
import java.util.stream.Collectors;  // Pour filtrer facilement
import model.Evaluation;             


public class GestionEvaluations {
    
    // grande liste où on stocke toutes les évaluations
    private ArrayList<Evaluation> evaluations = new ArrayList<>();
    
    // Noms des fichiers/dossiers (constants = ne changent jamais)
    private static final String FICHIER_SAUVEGARDE = "evaluations_ihec.dat";  // Fichier pour sauvegarder
    private static final String DOSSIER_CORRIGES = "corriges";                // Dossier pour les corrigés

   
    public GestionEvaluations() {
        creerDossierCorriges();  // 1. Crée le dossier corriges/
        chargerEvaluations();    // 2. Charge les anciennes évaluations
    }

    
    // Crée le dossier corriges/ s'il n'existe pas
     
    private void creerDossierCorriges() {
        File dossier = new File(DOSSIER_CORRIGES);  // On regarde le dossier "corriges"
        if (!dossier.exists()) {                    // S'il n'existe PAS
            if (dossier.mkdir()) {                  // On le CRÉE
                System.out.println("✅ Dossier 'corriges/' créé avec succès");
            } else {
                System.err.println("❌ Impossible de créer le dossier 'corriges/'");
            }
        }
    }

    //  Copie un fichier dans corriges/ et donne le nouveau chemin
     
    public String copierFichierDansCorriges(String cheminOriginal, String nomEvaluation) {
        //  Vérifier si le fichier existe 
        if (cheminOriginal == null || cheminOriginal.isEmpty()) {
            return null;
        }

        try {
            File fichierOriginal = new File(cheminOriginal);
            if (!fichierOriginal.exists()) {
                System.err.println("❌ Le fichier source n'existe pas: " + cheminOriginal);
                return null;
            }

            //  Crée un nom unique 
            String extension = "";
            String nomFichier = fichierOriginal.getName();
            int indexPoint = nomFichier.lastIndexOf('.');
            if (indexPoint > 0) {
                extension = nomFichier.substring(indexPoint);  // .pdf, .doc, etc.
            }

            String nomNettoye = nomEvaluation.replaceAll("[^a-zA-Z0-9]", "_");  // Remplace espaces par _
            String nouveauNom = nomNettoye + "_" + System.currentTimeMillis() + extension;

            // Copie le fichier dans corriges/
            File fichierDestination = new File(DOSSIER_CORRIGES, nouveauNom);
            Files.copy(
                fichierOriginal.toPath(),
                fichierDestination.toPath(),
                StandardCopyOption.REPLACE_EXISTING  // Écrase s'il existe déjà
            );

            System.out.println("✅ Fichier copié: " + fichierDestination.getAbsolutePath());
            return fichierDestination.getAbsolutePath();  // Retourne le nouveau chemin

        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la copie du fichier: " + e.getMessage());
            return null;
        }
    }

    //  ajouter une évaluation dans la liste + sauvegarder
    public void ajouterEvaluation(Evaluation e) {
        evaluations.add(e);           //  met dans la liste
        sauvegarderEvaluations();     //  sauvegarde sur disque
    }

    //  supprimer une évaluation + son fichier + sauvegarder
    public void supprimerEvaluation(Evaluation e) {
        // Supprime le fichier joint s'il existe
        if (e.aFichierJoint()) {
            File fichier = new File(e.getCheminFichier());
            if (fichier.exists() && fichier.delete()) {
                System.out.println("✅ Fichier joint supprimé: " + fichier.getName());
            }
        }

        evaluations.remove(e);        // enlève de la liste
        sauvegarderEvaluations();     // sauvegarde
    }

    //  Récupère toute la liste
    public ArrayList<Evaluation> getEvaluations() {
        return evaluations;
    }

    //  filtre  : trouve les évaluations qui correspondent
    public ArrayList<Evaluation> filtrer(String filiere, String niveau, String matiere) {
        return evaluations.stream()
            // garde seulement  celles qui matchent
            .filter(e -> (filiere.isEmpty() || e.getMatiere().getFiliere().toLowerCase().contains(filiere.toLowerCase())) &&
                        (niveau.isEmpty() || e.getMatiere().getNiveau().toLowerCase().contains(niveau.toLowerCase())) &&
                        (matiere.isEmpty() || e.getMatiere().getNom().toLowerCase().contains(matiere.toLowerCase())))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    //  sauvegarde la liste dans un fichier (automatique)
    private void sauvegarderEvaluations() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_SAUVEGARDE))) {
            oos.writeObject(evaluations);  
            System.out.println("✅ Évaluations sauvegardées avec succès");
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    //  charge les évaluations depuis le fichier (automatique)
    @SuppressWarnings("unchecked")
    private void chargerEvaluations() {
        File fichier = new File(FICHIER_SAUVEGARDE);
        if (!fichier.exists()) {
            System.out.println("ℹ️ Aucun fichier de sauvegarde trouvé. Démarrage avec une liste vide.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_SAUVEGARDE))) {
            evaluations = (ArrayList<Evaluation>) ois.readObject();  
            System.out.println("✅ " + evaluations.size() + " évaluation(s) chargée(s) depuis le fichier");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Erreur lors du chargement: " + e.getMessage());
            evaluations = new ArrayList<>();  // Liste vide si erreur
        }
    }

    //  Compteur rapide
    public int getNombreEvaluations() {
        return evaluations.size();
    }
}
