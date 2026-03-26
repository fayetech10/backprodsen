package com.example.sen_scu.model.sen_csu;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@ToString(exclude = {"adherent", "projet"})
public class PersonneCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne
    @JoinColumn(name="adherent_id")
    @JsonBackReference(value = "adherent-personneCharge")
    private Adherent adherent;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    @JsonBackReference(value = "projet-personneCharge")
    private Projet projet;

    @ManyToOne
    @JsonBackReference(value = "agent-personneCharge")
    @JoinColumn(name = "agent_id")
    private Agent agent;

}
