package com.example.sen_scu.dto.sen_csu;

import lombok.Data;

import java.util.List;

@Data
public class PaiementRequest {

    private Double montant;
    private String reference;
    private String photoPaiement;
    private List<String> photos;
    private String modePaiement;
    private Long adherentId;
}
