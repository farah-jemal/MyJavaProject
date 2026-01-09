package model;

import java.io.Serializable;//importe quelquechose pour sauvegarder une chose dans un fichier

public class Compte implements Serializable { //compte(mail+mdp) sauvgarder dans un fichier
    private static final long serialVersionUID = 1L;//num id pour la sauvgarde 
    
    private String email;
    private String motDePasse;
    private Utilisateur.Type type; //boite stock le type de user
    
    public Compte(String email, String motDePasse, Utilisateur.Type type) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public Utilisateur.Type getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return email + " (" + type + ")";
    }
}