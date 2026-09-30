package com.ihec.gestion.model;

import jakarta.persistence.*;

// représente un Devoir Surveillé, hérite de toute la logique d'Evaluation
@Entity
@DiscriminatorValue("DS")
public class DS extends Evaluation {

    public DS() {}

    public DS(String titre, Matiere matiere) {
        super(titre, matiere);
    }

    @Override
    public String getType() { return "DS"; }
}