package com.example.sen_scu.model.sen_csu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
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
    private String name, photo, dateNaissance, prenom, sexe, departement, commune, region, adresse, telephone, role;

    @Indexed(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Builder.Default
    private boolean active = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @DBRef
    @JsonIgnore
    @Builder.Default
    private List<Adherent> adherents = new ArrayList<>();

}
