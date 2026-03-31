package com.example.sen_scu.model.sen_csu;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "projets")
@Data
public class Projet {

    @Id
    private String id;

    private String nomProjet;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();

    @DBRef
    private Adherent adherent;

    @DBRef
    @JsonManagedReference(value = "projet-personneCharge")
    private List<PersonneCharge> personnesCharge = new ArrayList<>();
}
