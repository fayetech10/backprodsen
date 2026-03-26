package com.example.sen_scu.model.sen_csu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agent {
    @Id
    @GeneratedValue
    private Long id;
    private String name, photo,dateNaissance,prenom, sexe, departement, commune,region, email, adresse, telephone,role,password;

    @OneToMany(mappedBy = "agent", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Adherent> adherents = new ArrayList<>();

    


}
