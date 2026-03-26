package com.example.sen_scu.dto.sen_csu;

import com.example.sen_scu.model.sen_csu.PersonneCharge;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AdherentRequest {

    // Champs de l'adhérent (pas d'objet Adherent ici)
    private String prenoms;
    private String nom;
    private String adresse;
    private String lieuNaissance;
    private String sexe;
    private LocalDate dateNaissance;
    private String situationMatrimoniale;
    private String whatsapp;
    private String secteurActivite;
    private String numeroCNi;
    private String region;
    private String role;
    private String password;
    private String departement;
    private String commune;
    private Double montantTotal;
    private  String photo;
    private String photoRecto;
    private String typePiece;
    private String photoVerso;
    private String clientUUID;

    // Liste des personnes à charge
    private List<PersonneCharge> personnesCharge;
}
