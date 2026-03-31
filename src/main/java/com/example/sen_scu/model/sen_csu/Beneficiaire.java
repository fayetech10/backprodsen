package com.example.sen_scu.model.sen_csu;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "beneficiaires")
@Data
public class Beneficiaire {

    @Id
    private String id;

    private String agentCollect;
    private String nom;
    private String prenoms;
    private String region;
    private String assureur;
    private String typeBenef;
    private String typeAdhesion;
    private String departement;
    private String commune;
    private String sexe;
    private String dateNaissance;
    private String lieuNaissance;
    private String adresse;
    private String situationM;
    private String beneficiaire;
    private String regime;
    private String date;
}