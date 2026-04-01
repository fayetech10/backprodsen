package com.example.sen_scu.dto.sen_csu;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentCreateRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @Size(min = 4, message = "Le mot de passe doit contenir au moins 4 caractères")
    private String password;

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
