package com.example.sen_scu.model.sen_csu;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "adherents")
@Data
@ToString(exclude = { "personnesCharge", "agent" })
public class Adherent {

    @Id
    private String id;

    private String prenoms;
    private String nom;
    private String sexe;
    private String regime = "CONTRIBUTIF";
    private String region;
    private String departement;
    private String commune;
    private String typeBenef = "CLASSIQUE";
    private String typeAdhesion = "FAMALE";
    private LocalDate dateNaissance;
    private String whatsapp;
    private String lieuNaissance;
    private String situationMatrimoniale;
    private String adresse;
    private String password;
    private String role;
    private String lienParent;
    private String photo;
    private String photoRecto;
    private String photoVerso;
    private String situationM;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Double montantTotal = 0.0;

    @Indexed(unique = true)
    private String clientUUID;

    @Indexed(unique = true)
    private String numeroCNi;
    private String typePiece;

    private String secteurActivite;

    @DBRef
    private List<PersonneCharge> personnesCharge = new ArrayList<>();

    @DBRef
    private PaiementCotisation paiementCotisation;

    @DBRef
    private Agent agent;

}
