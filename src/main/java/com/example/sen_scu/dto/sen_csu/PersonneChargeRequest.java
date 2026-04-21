package com.example.sen_scu.dto.sen_csu;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PersonneChargeRequest {

    @NotBlank
    private String prenoms;

    @NotBlank
    private String nom;
    private String sexe;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String adresse;
    @com.fasterxml.jackson.annotation.JsonProperty("whatsapp")
    private String telephone;
    private String lienParent;
    private String photo;
    private String photoRecto;
    private String photoVerso;
    private String situationM;
    private String numeroCNi; // facultatif
    private String numeroExtrait; // facultatif
}
