package com.example.sen_scu.model.sen_csu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agent {
    @Id
    private String id;
    private String name, photo, dateNaissance, prenom, sexe, departement, commune, region, email, adresse, telephone,
            role, password;

    @DBRef
    @JsonIgnore
    private List<Adherent> adherents = new ArrayList<>();

}
