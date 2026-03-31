package com.example.sen_scu.model.sen_csu;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "personnesCharge")
@Data
@ToString(exclude = {"adherent", "projet"})
public class PersonneCharge {

    @Id
    private String id;

    private String prenoms;
    private String nom;
    private String sexe;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String adresse;
    private String whatsapp;
    private String lienParent;
    private String photo;
    private String photoRecto;
    private String photoVerso;
    private String situationM;
    private LocalDateTime createdAt = LocalDateTime.now();

    private String numeroCNi;
    private String numeroExtrait;

    @DBRef
    @JsonBackReference(value = "adherent-personneCharge")
    private Adherent adherent;

    @DBRef
    @JsonBackReference(value = "projet-personneCharge")
    private Projet projet;

    @DBRef
    @JsonBackReference(value = "agent-personneCharge")
    private Agent agent;

}
