package com.example.sen_scu.dto.sen_csu;

import com.example.sen_scu.model.sen_csu.Agent;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjetWithAdherentRequest {
    // Projet
    private String nomProjet;
    private String description;

    // Adhérent
    private String prenoms;
    private String nom;
    private String sexe;
    private String regime;
    private String region;
    private String departement;
    private String commune;
    private String typeBenef;
    private String typeAdhesion;
    private LocalDate dateNaissance;
    private String whatsapp;
    private String typePiece;
    private String lieuNaissance;
    private String adresse;
    private String lienParent;
    private String photo;
    private String photoRecto;
    private String photoVerso;
    private String situationM;
    private String numeroCNi;
    private String numeroExtrait;
    private String secteurActivite;

    private Long agentId;
}
