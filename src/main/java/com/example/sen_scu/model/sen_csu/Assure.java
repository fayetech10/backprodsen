package com.example.sen_scu.model.sen_csu;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "assures")
@Data
public class Assure {
    @Id
    private String id;

    private String dateEnregistrement;
    private String codeImmatriculation;
    private String noms;
    private String prenoms;
    private String dateNaissance;
    private String sexe;
    private String telephone;
    private String adresse;
    private String regime;
    private String assureur;
    private String typeBenef;
    private String dateCotisation;
    private String dateFinCotisation;
    private String qrCodeUrl;
    private String region;
    private String departement;
    private String commune;
    private String groupe;
    private String typeAdhesion;
    private String typeCotisation;
    private String cni;
    private String photo;

    // Agent collecteur
    private String agentCollect;

    // Carte tracking fields (not imported from Excel, managed internally)
    private String carteAssure;
    private String dateRemise;

    // Grouping by Excel filename
    private String importName;
}
