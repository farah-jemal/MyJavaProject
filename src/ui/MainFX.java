package ui; 

import javafx.application.Application;      // Lanceur d'app JavaFX
import javafx.geometry.Insets;             // Marges
import javafx.geometry.Pos;                // Alignements
import javafx.scene.Scene;                 // Fenêtre
import javafx.scene.control.*;             // Boutons, champs, listes
import javafx.scene.layout.*;              // Boîtes (VBox, HBox)
import javafx.stage.FileChooser;           // Sélecteur de fichiers
import javafx.stage.Stage;                 // Fenêtre principale
import model.*;                            
import service.GestionComptes;            
import service.GestionEvaluations;         
import java.io.File;                       // Fichiers


public class MainFX extends Application {
    
   
    private GestionEvaluations gestion = new GestionEvaluations();     // Gère DS/Exams
    private GestionComptes gestionComptes = new GestionComptes();     // Gère connexions

  
    @Override
    public void start(Stage stage) {  // Stage = la grande fenêtre
        // ÉCRAN D'ACCUEIL (choix Étudiant/Prof)
        VBox root = new VBox(20);                    // Boîte verticale (éléments empilés)
        root.setPadding(new Insets(30));             // Marges 30px partout
        root.setAlignment(Pos.CENTER);               // Centre tout
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #C5CAE9);");  // Fond dégradé bleu
        
        // TITRE PRINCIPAL
        Label titre = new Label("Gestion DS & Examens - IHEC");
        titre.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");  // Gros, gras, bleu foncé
        
        // Sous-titre
        Label sousTitre = new Label("Choisir le type d'utilisateur");
        sousTitre.setStyle("-fx-font-size: 16px; -fx-text-fill: #3949AB;");  // Plus petit, bleu moyen
        
        // BOUTON ÉTUDIANT (bleu)
        Button btnEtudiant = new Button("Je suis Étudiant");
        btnEtudiant.setStyle("-fx-font-size: 16px; -fx-background-color: #2196F3; -fx-text-fill: white; " +
                "-fx-padding: 15 40; -fx-background-radius: 10; -fx-cursor: hand;");  // Design arrondi
        btnEtudiant.setOnAction(e -> showLoginUI(stage, Utilisateur.Type.ETUDIANT));  // CLIC → écran connexion étudiant
        
        // BOUTON PROF (violet)
        Button btnProf = new Button("Je suis Professeur");
        btnProf.setStyle("-fx-font-size: 16px; -fx-background-color: #5E35B1; -fx-text-fill: white; " +
                "-fx-padding: 15 40; -fx-background-radius: 10; -fx-cursor: hand;");
        btnProf.setOnAction(e -> showLoginUI(stage, Utilisateur.Type.PROF));  // CLIC → écran connexion prof
        
        // COMPTEUR TOTAL (en bas)
        Label compteur = new Label("Total: " + gestion.getNombreEvaluations() + " évaluation(s) enregistrée(s)");
        compteur.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");  // Petit, gris
        
        // ASSEMBLE TOUT dans la boîte verticale
        root.getChildren().addAll(titre, sousTitre, btnEtudiant, btnProf, compteur);
        
        // CRÉE LA FENÊTRE 500x400
        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.setTitle("Gestion DS & Examens - IHEC");
        stage.show();  // OUVRE L'APP !
    }

    // ÉCRAN DE CONNEXION (Étudiant OU Prof)
    private void showLoginUI(Stage stage, Utilisateur.Type type) {
        VBox root = new VBox(20);                    // Nouvelle boîte verticale
        root.setPadding(new Insets(40));             // Plus de marges
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #C5CAE9);");  // Même fond
        
        // TITRE (dynamique)
        String typeText = type == Utilisateur.Type.ETUDIANT ? "Étudiant" : "Professeur";
        Label titre = new Label("Connexion " + typeText);
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        // FORMULAIRE (boîte blanche avec ombre)
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setAlignment(Pos.CENTER);
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");  // Ombre portée
        formBox.setMaxWidth(400);  // Largeur max
        
        // CHAMP EMAIL
        Label lblEmail = new Label("Email:");
        lblEmail.setStyle("-fx-font-weight: bold;");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("exemple@ihec.tn");  // Texte fantôme
        txtEmail.setPrefWidth(300);  // Largeur
        
        // CHAMP MOT DE PASSE
        Label lblMdp = new Label("Mot de passe:");
        lblMdp.setStyle("-fx-font-weight: bold;");
        PasswordField txtMdp = new PasswordField();  // Masque les caractères
        txtMdp.setPromptText("Votre mot de passe");
        txtMdp.setPrefWidth(300);
        
        // BOUTONS HORIZONTAUX
        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER);
        
        Button btnConnexion = new Button("Se connecter");  // Vert
        btnConnexion.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30;");
        
        Button btnInscrire = new Button("S'inscrire");     // Bleu
        btnInscrire.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30;");
        
        btnBox.getChildren().addAll(btnConnexion, btnInscrire);
        
        // BOUTON RETOUR
        Button btnRetour = new Button("Retour");
        btnRetour.setStyle("-fx-background-color: transparent; -fx-text-fill: #757575;");
        btnRetour.setOnAction(e -> start(stage));  // Retour à l'accueil
        
        // INFO COMPTES TEST
        Label infoComptes = new Label("Comptes de test:\n" +
                "Étudiant: etudiant@ihec.tn / etudiant123\n" +
                "Prof: prof@ihec.tn / prof123");
        infoComptes.setStyle("-fx-font-size: 10px; -fx-text-fill: #757575; -fx-text-alignment: center;");
        
        // ASSEMBLE LE FORMULAIRE
        formBox.getChildren().addAll(lblEmail, txtEmail, lblMdp, txtMdp, btnBox);
        root.getChildren().addAll(titre, formBox, infoComptes, btnRetour);
        
        // ACTION : CLIC "SE CONNECTER"
        btnConnexion.setOnAction(e -> {
            String email = txtEmail.getText().trim();  // Enlève espaces
            String mdp = txtMdp.getText();
            
            // VÉRIFICATIONS
            if (email.isEmpty() || mdp.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs!");
                alert.showAndWait();
                return;  // Arrête si vide
            }
            
            // TENTATIVE CONNEXION
            Compte compte = gestionComptes.authentifier(email, mdp, type);
            
            if (compte != null) {  // SUCCÈS
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Connexion réussie!\nBienvenue " + email);
                alert.showAndWait();
                
                // REDIRECTION
                if (type == Utilisateur.Type.ETUDIANT) {
                    showEtudiantUI(stage, compte);  // → Interface étudiant
                } else {
                    showProfUI(stage, compte);      // → Interface prof
                }
            } else {  // ÉCHEC
                Alert alert = new Alert(Alert.AlertType.ERROR, "Email ou mot de passe incorrect!\nOu vous n'avez pas le bon type de compte.");
                alert.showAndWait();
            }
        });
        
        // ACTION : CLIC "S'INSCRIRE"
        btnInscrire.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String mdp = txtMdp.getText();
            
            // VALIDATIONS
            if (email.isEmpty() || mdp.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs!");
                alert.showAndWait();
                return;
            }
            if (!email.contains("@")) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Email invalide!");
                alert.showAndWait();
                return;
            }
            if (mdp.length() < 6) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Le mot de passe doit contenir au moins 6 caractères!");
                alert.showAndWait();
                return;
            }
            
            // INSCRIPTION
            if (gestionComptes.inscrire(email, mdp, type)) {  // OK
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Inscription réussie!\nVous pouvez maintenant vous connecter.");
                alert.showAndWait();
                txtMdp.clear();  // Vide le mot de passe
            } else {  // Email existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cet email existe déjà!");
                alert.showAndWait();
            }
        });
        
        // CONNEXION AVEC ENTRÉE
        txtMdp.setOnAction(e -> btnConnexion.fire());  // Simule clic
        
        // NOUVELLE FENÊTRE 550x550
        Scene scene = new Scene(root, 550, 550);
        stage.setScene(scene);  // Remplace l'écran précédent
    }
    
    // INTERFACE ÉTUDIANT (3 COLONNES)
    private void showEtudiantUI(Stage stage, Compte compte) {
        BorderPane root = new BorderPane();          // Layout bordure
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #F5F5F5;");  // Gris clair

        // EN-TÊTE (Header)
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label titre = new Label("Espace Étudiant - " + compte.getEmail());
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1976D2;");
        Button btnRetour = new Button("Déconnexion");
        btnRetour.setOnAction(e -> start(stage));    // Retour accueil
        Region spacer = new Region();                // Espaceur
        HBox.setHgrow(spacer, Priority.ALWAYS);      // Pousse le bouton à droite
        header.getChildren().addAll(titre, spacer, btnRetour);

        // COLONNE 1 : FILTRES (GAUCHE)
        VBox filtreBox = new VBox(10);
        filtreBox.setPadding(new Insets(15));
        filtreBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label lblFiltres = new Label("Filtres de Recherche");
        lblFiltres.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField txtFiliere = new TextField();
        txtFiliere.setPromptText("Filière (ex: Finance, Comptabilité...)");

        TextField txtNiveau = new TextField();
        txtNiveau.setPromptText("Niveau (ex: L1, L2, L3, M1...)");

        TextField txtMatiere = new TextField();
        txtMatiere.setPromptText("Matière (ex: Mathématiques, Économie...)");

        Button btnFiltrer = new Button("Filtrer");
        btnFiltrer.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        filtreBox.getChildren().addAll(
            lblFiltres,
            new Label("Filière:"), txtFiliere,
            new Label("Niveau:"), txtNiveau,
            new Label("Matière:"), txtMatiere,
            btnFiltrer
        );

        // COLONNE 2 : LISTE (CENTRE - prend plus de place)
        VBox listeBox = new VBox(10);
        listeBox.setPadding(new Insets(15));
        listeBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label lblListe = new Label("Liste des Évaluations");
        lblListe.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Evaluation> listView = new ListView<>();  // Liste cliquable
        listView.setPrefHeight(300);
        listView.getItems().addAll(gestion.getEvaluations());  // Charge toutes les évals

        listeBox.getChildren().addAll(lblListe, listView);

        // COLONNE 3 : CORRIGÉ (DROITE)
        VBox corrigeBox = new VBox(10);
        corrigeBox.setPadding(new Insets(15));
        corrigeBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label lblCorrige = new Label("Corrigé");
        lblCorrige.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea corrigeArea = new TextArea();       // Zone de texte (lecture seule)
        corrigeArea.setEditable(false);
        corrigeArea.setWrapText(true);
        corrigeArea.setPrefHeight(250);
        corrigeArea.setPromptText("Sélectionnez une évaluation pour voir son corrigé...");

        HBox btnBox = new HBox(10);
        Button btnVoir = new Button("Voir le Corrigé");     // Vert
        btnVoir.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnOuvrirFichier = new Button("Ouvrir le Fichier Joint");
        btnOuvrirFichier.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        btnOuvrirFichier.setDisable(true);

        btnBox.getChildren().addAll(btnVoir, btnOuvrirFichier);
        corrigeBox.getChildren().addAll(lblCorrige, corrigeArea, btnBox);

        // ACTION FILTRE
        btnFiltrer.setOnAction(e -> {
            listView.getItems().clear();
            listView.getItems().addAll(
                gestion.filtrer(txtFiliere.getText(), txtNiveau.getText(), txtMatiere.getText())
            );
        });

        // ACTION VOIR CORRIGÉ
        btnVoir.setOnAction(e -> {
            Evaluation ev = listView.getSelectionModel().getSelectedItem();
            if (ev != null) {
                corrigeArea.setText(ev.getType() + ": " + ev.getTitre() + "\n" +
                        ev.getMatiere().getNom() + "\n" +
                        ev.getMatiere().getFiliere() + " - " + ev.getMatiere().getNiveau() + "\n\n" +
                        "CORRECTION:\n" +
                        "─────────────────────────\n" +
                        ev.getCorrige());
                btnOuvrirFichier.setDisable(!ev.aFichierJoint());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une évaluation!");
                alert.showAndWait();
            }
        });

        // ACTION OUVRIR FICHIER
        btnOuvrirFichier.setOnAction(e -> {
            Evaluation ev = listView.getSelectionModel().getSelectedItem();
            if (ev != null && ev.aFichierJoint()) {
                ouvrirFichier(ev.getCheminFichier());
            }
        });

        // ASSEMBLAGE 3 COLONNES
        HBox content = new HBox(15);
        content.getChildren().addAll(filtreBox, listeBox, corrigeBox);
        HBox.setHgrow(listeBox, Priority.ALWAYS);
        HBox.setHgrow(corrigeBox, Priority.ALWAYS);

        VBox main = new VBox(15, header, content);
        root.setCenter(main);

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
    }
    
    // INTERFACE PROFESSEUR (2 COLONNES)
    private void showProfUI(Stage stage, Compte compte) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #F5F5F5;");

        // EN-TÊTE
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label titre = new Label("Espace Professeur - " + compte.getEmail());
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #5E35B1;");
        Button btnRetour = new Button("Déconnexion");
        btnRetour.setOnAction(e -> start(stage));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(titre, spacer, btnRetour);

        // COLONNE 1 : FORMULAIRE AJOUT (GAUCHE)
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label lblForm = new Label("Ajouter une Évaluation");
        lblForm.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("DS", "Examen");
        typeBox.setValue("DS");
        typeBox.setPrefWidth(200);

        TextField txtTitre = new TextField();
        txtTitre.setPromptText("Titre (ex: DS N°1 Semestre 1)");

        TextField txtMatiere = new TextField();
        txtMatiere.setPromptText("Matière (ex: Mathématiques Financières)");

        TextField txtNiveau = new TextField();
        txtNiveau.setPromptText("Niveau (ex: L2)");

        TextField txtFiliere = new TextField();
        txtFiliere.setPromptText("Filière (ex: Finance)");

        TextArea txtCorrige = new TextArea();
        txtCorrige.setPromptText("Saisissez le corrigé détaillé...");
        txtCorrige.setPrefHeight(150);
        txtCorrige.setWrapText(true);

        // SECTION FICHIER
        Label lblFichier = new Label("Fichier Joint (PDF/DOCX) - Sera copié automatiquement :");
        lblFichier.setStyle("-fx-font-weight: bold;");

        HBox fichierBox = new HBox(10);
        TextField txtFichier = new TextField();
        txtFichier.setPromptText("Aucun fichier sélectionné");
        txtFichier.setEditable(false);
        txtFichier.setPrefWidth(300);

        Button btnParcourir = new Button("Parcourir");
        btnParcourir.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");

        Button btnEffacerFichier = new Button("X");
        btnEffacerFichier.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        btnEffacerFichier.setDisable(true);

        fichierBox.getChildren().addAll(txtFichier, btnParcourir, btnEffacerFichier);

        Button btnAjouter = new Button("Ajouter l'Évaluation");
        btnAjouter.setStyle("-fx-background-color: #5E35B1; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        formBox.getChildren().addAll(
            lblForm,
            new Label("Type:"), typeBox,
            new Label("Titre:"), txtTitre,
            new Label("Matière:"), txtMatiere,
            new Label("Niveau:"), txtNiveau,
            new Label("Filière:"), txtFiliere,
            new Label("Enoncé et Correction:"), txtCorrige,
            lblFichier, fichierBox,
            btnAjouter
        );

        // COLONNE 2 : LISTE (DROITE)
        VBox listeBox = new VBox(10);
        listeBox.setPadding(new Insets(15));
        listeBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label lblListe = new Label("Liste des Évaluations (" + gestion.getNombreEvaluations() + ")");
        lblListe.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Evaluation> listView = new ListView<>();
        listView.setPrefHeight(400);
        listView.getItems().addAll(gestion.getEvaluations());

        HBox btnListeBox = new HBox(10);
        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnOuvrirFichier = new Button("Ouvrir Fichier");
        btnOuvrirFichier.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        btnOuvrirFichier.setDisable(true);

        btnListeBox.getChildren().addAll(btnSupprimer, btnOuvrirFichier);
        listeBox.getChildren().addAll(lblListe, listView, btnListeBox);

        // ACTION PARCOURIR FICHIER
        final String[] cheminFichierSelectionne = {null};

        btnParcourir.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner le corrigé (PDF/DOCX)");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Fichiers Word", "*.docx", "*.doc"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
            );
            File fichier = fileChooser.showOpenDialog(stage);
            if (fichier != null) {
                cheminFichierSelectionne[0] = fichier.getAbsolutePath();
                txtFichier.setText(fichier.getName() + " ✓");
                btnEffacerFichier.setDisable(false);
            }
        });

        btnEffacerFichier.setOnAction(e -> {
            cheminFichierSelectionne[0] = null;
            txtFichier.clear();
            txtFichier.setPromptText("Aucun fichier sélectionné");
            btnEffacerFichier.setDisable(true);
        });

        // ACTION AJOUTER ÉVALUATION
        btnAjouter.setOnAction(e -> {
            if (txtTitre.getText().isEmpty() || txtMatiere.getText().isEmpty() ||
                txtNiveau.getText().isEmpty() || txtFiliere.getText().isEmpty() ||
                txtCorrige.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires!");
                alert.showAndWait();
                return;
            }

            Matiere m = new Matiere(txtMatiere.getText(), txtNiveau.getText(), txtFiliere.getText());
            Evaluation ev = typeBox.getValue().equals("DS") ?
                new DS(txtTitre.getText(), m, txtCorrige.getText()) :
                new Examen(txtTitre.getText(), m, txtCorrige.getText());

            // COPIER FICHIER SI PRÉSENT
            if (cheminFichierSelectionne[0] != null) {
                String nouveauChemin = gestion.copierFichierDansCorriges(
                    cheminFichierSelectionne[0],
                    txtTitre.getText()
                );
                if (nouveauChemin != null) {
                    ev.setCheminFichier(nouveauChemin);
                    System.out.println("Fichier copié dans le dossier corriges/");
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Impossible de copier le fichier, mais l'évaluation sera ajoutée sans fichier joint.");
                    alert.showAndWait();
                }
            }

            gestion.ajouterEvaluation(ev);
            listView.getItems().add(ev);
            lblListe.setText("Liste des Évaluations (" + gestion.getNombreEvaluations() + ")");

            // RÉINITIALISER FORMULAIRE
            txtTitre.clear();
            txtMatiere.clear();
            txtNiveau.clear();
            txtFiliere.clear();
            txtCorrige.clear();
            cheminFichierSelectionne[0] = null;
            txtFichier.clear();
            txtFichier.setPromptText("Aucun fichier sélectionné");
            btnEffacerFichier.setDisable(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Évaluation ajoutée et sauvegardée avec succès!\n" +
                (ev.aFichierJoint() ? "Fichier copié dans le dossier 'corriges/'" : ""));
            alert.showAndWait();
        });

        // ACTIVE BOUTON OUVRIR FICHIER
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnOuvrirFichier.setDisable(newVal == null || !newVal.aFichierJoint());
        });

        // ACTION OUVRIR FICHIER LISTE
        btnOuvrirFichier.setOnAction(e -> {
            Evaluation ev = listView.getSelectionModel().getSelectedItem();
            if (ev != null && ev.aFichierJoint()) {
                ouvrirFichier(ev.getCheminFichier());
            }
        });

        // ACTION SUPPRIMER
        btnSupprimer.setOnAction(e -> {
            Evaluation ev = listView.getSelectionModel().getSelectedItem();
            if (ev != null) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                    "Voulez-vous vraiment supprimer cette évaluation?\n" +
                    (ev.aFichierJoint() ? "Le fichier joint sera également supprimé du dossier 'corriges/'" : ""));
                confirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        gestion.supprimerEvaluation(ev);
                        listView.getItems().remove(ev);
                        lblListe.setText("Liste des Évaluations (" + gestion.getNombreEvaluations() + ")");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Évaluation supprimée avec succès!" +
                            (ev.aFichierJoint() ? "\nFichier joint également supprimé" : ""));
                        alert.showAndWait();
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une évaluation à supprimer!");
                alert.showAndWait();
            }
        });

        // ASSEMBLAGE 2 COLONNES
        HBox content = new HBox(15);
        content.getChildren().addAll(formBox, listeBox);
        HBox.setHgrow(listeBox, Priority.ALWAYS);

        VBox main = new VBox(15, header, content);
        root.setCenter(main);

        Scene scene = new Scene(root, 1200, 750);
        stage.setScene(scene);
    }
    
    // OUVRE FICHIER AVEC APP PAR DÉFAUT
    private void ouvrirFichier(String cheminFichier) {
        File fichier = new File(cheminFichier);
        if (!fichier.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                "Le fichier n'existe plus à l'emplacement:\n" + cheminFichier);
            alert.showAndWait();
            return;
        }

        try {
            getHostServices().showDocument(fichier.getAbsolutePath());  // Ouvre PDF/Word
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                "Erreur lors de l'ouverture du fichier:\n" + ex.getMessage());
            alert.showAndWait();
        }
    }
    
    public static void main(String[] args) {
        launch();  // DÉMARRE L'APPLICATION !
    }
}
