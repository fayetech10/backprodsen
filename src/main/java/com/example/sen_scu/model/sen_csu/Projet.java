package com.example.sen_scu.model.sen_csu;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomProjet;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();

    // 🔥 Le projet est associé à UN SEUL adhérent
    @ManyToOne
    @JoinColumn(name = "adherent_id")
    private Adherent adherent;

    // 🔥 Toutes les personnes ajoutées dans le projet seront stockées ici
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "projet-personneCharge")
    private List<PersonneCharge> personnesCharge = new ArrayList<>();
}
