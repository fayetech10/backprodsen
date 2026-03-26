package com.example.sen_scu.model.sen_csu;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Beneficiaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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