package com.example.sen_scu.dto.sen_csu.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenoms;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
    private String whatsapp;

    private String dateNaissance;
    private String lieuNaissance;
    private String adresse;
    private String sexe;
    private String region;
    private String departement;
    private String commune;
    private String role;
}