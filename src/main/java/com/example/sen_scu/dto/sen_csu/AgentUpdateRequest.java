package com.example.sen_scu.dto.sen_csu;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentUpdateRequest {
    private String name;
    private String prenom;

    @Email(message = "Format d'email invalide")
    private String email;

    private String telephone;
    private String photo;
    private String dateNaissance;
    private String sexe;
    private String departement;
    private String commune;
    private String region;
    private String adresse;
    private String role;
}
