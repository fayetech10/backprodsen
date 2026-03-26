package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.PaiementRequest;
import com.example.sen_scu.model.sen_csu.PaiementCotisation;

import java.util.List;

public interface PaiementService {
  PaiementCotisation addPaiement(PaiementRequest request);
  List<PaiementCotisation> getAllPaiementsByAdherentId(Long adherentId);
}
