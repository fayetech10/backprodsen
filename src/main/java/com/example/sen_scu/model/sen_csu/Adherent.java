package com.example.sen_scu.model.sen_csu;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = { "personnesCharge", "agent" }) // 👈 IMPORTANT
@Table(name = "adherent", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "numeroCNi" })
})
public class Adherent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(unique = true, nullable = false)
    private String clientUUID;

    @Column(unique = true)
    private String numeroCNi;
    private String typePiece;

    private String secteurActivite;

    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "adherent-personneCharge")
    private List<PersonneCharge> personnesCharge = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "paiement_id")
    private PaiementCotisation paiementCotisation;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

}
