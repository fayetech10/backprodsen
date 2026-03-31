package com.example.sen_scu.model.sen_csu;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "paiements")
public class PaiementCotisation {

    @Id
    private String id;

    private Double montant;
    private LocalDateTime datePaiement = LocalDateTime.now();

    @Indexed(unique = true)
    private String reference;

    private List<String> photos = new ArrayList<>();
    private String photoPaiement = "";
    private String modePaiement;

    @DBRef
    private Adherent adherent;

}
