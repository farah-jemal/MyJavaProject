package com.ihec.gestion.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Examen")
public class Examen extends Evaluation {

    public Examen() {}

    public Examen(String titre, Matiere matiere) {
        super(titre, matiere);
    }

    @Override
    public String getType() { return "Examen"; }
}