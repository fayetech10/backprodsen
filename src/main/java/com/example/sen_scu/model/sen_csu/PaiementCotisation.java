package com.example.sen_scu.model.sen_csu;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "paiement_cotisation", uniqueConstraints = {
                @UniqueConstraint(columnNames = "reference")
})
public class PaiementCotisation {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Double montant;
        private LocalDateTime datePaiement = LocalDateTime.now();

        @Column(unique = true)
        private String reference;

        @ElementCollection
        @CollectionTable(name = "paiement_photos", joinColumns = @JoinColumn(name = "paiement_id"))
        @Column(name = "photo")
        private List<String> photos = new ArrayList<>();
        private String photoPaiement = "";
        private String modePaiement;

        @OneToOne(cascade = CascadeType.ALL)
        private Adherent adherent;

}
