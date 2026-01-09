package service;

import java.io.*; // importe les fichier (ecriture / lecture)
import java.util.ArrayList;
import model.Compte;
import model.Utilisateur;

public class GestionComptes {
    private ArrayList<Compte> comptes = new ArrayList<>();
    private static final String FICHIER_COMPTES = "comptes_ihec.dat";
    
    public GestionComptes() {
        chargerComptes();
        // Créer des comptes par défaut si aucun n'existe
        if (comptes.isEmpty()) {
            creerComptesParDefaut();
        }
    }
    
    //Crée des comptes de test par défaut
     
    private void creerComptesParDefaut() {
        comptes.add(new Compte("etudiant@ihec.tn", "etudiant123", Utilisateur.Type.ETUDIANT));
        comptes.add(new Compte("prof@ihec.tn", "prof123", Utilisateur.Type.PROF));
        sauvegarderComptes();
        System.out.println("✅ Comptes par défaut créés:");
        System.out.println("   - etudiant@ihec.tn / etudiant123");
        System.out.println("   - prof@ihec.tn / prof123");
    }
    
    
    //  Inscrit un nouveau compte
     
    public boolean inscrire(String email, String motDePasse, Utilisateur.Type type) {
        // Vérifier si l'email existe déjà
        if (emailExiste(email)) {
            return false;
        }
        
        comptes.add(new Compte(email, motDePasse, type));
        sauvegarderComptes();
        return true;
    }
    
    //Authentifie un utilisateur
     
    public Compte authentifier(String email, String motDePasse, Utilisateur.Type typeAttendu) {
        for (Compte compte : comptes) {
            if (compte.getEmail().equalsIgnoreCase(email) && 
                compte.getMotDePasse().equals(motDePasse) &&
                compte.getType() == typeAttendu) {
                return compte;
            }
        }
        return null;
    }
    
    //Vérifie si un email existe déjà
     
    public boolean emailExiste(String email) {
        return comptes.stream() //se transforme la liste en flux pour parcourir la liste
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email)); //pour chaque compte  verifie l email correspond au chaque compte 
    }
    
    private void sauvegarderComptes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_COMPTES))) { //objectoutputstream=outil pour ecrire des objets dans un fichier
        	//fileoutputstream = ouvre le fichier pour ecrire dedans 
            oos.writeObject(comptes); //affiche un message de confirmation dans la console
            System.out.println("✅ Comptes sauvegardés");
        } catch (IOException e) { //affiche un message d erreur en rouge dans la console et catch est un methode de detection d erreurs 
            System.err.println("❌ Erreur sauvegarde comptes: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void chargerComptes() {
        File fichier = new File(FICHIER_COMPTES); //cree un objet file qui represente le fichier sur le disque
        if (!fichier.exists()) { //verifie si le fichier existe sur le disque 
            System.out.println("ℹ️ Aucun fichier de comptes trouvé");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_COMPTES))) { //pour lire
            comptes = (ArrayList<Compte>) ois.readObject();
            System.out.println("✅ " + comptes.size() + " compte(s) chargé(s)");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Erreur chargement comptes: " + e.getMessage());
            comptes = new ArrayList<>(); //creer un liste vide pour eviter un crash
        }
    }
}