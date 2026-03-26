package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.PaiementRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.PaiementCotisation;
import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import com.example.sen_scu.repository.sen_csu.PaiementRepository;
import com.example.sen_scu.service.sen_csu.exception.AdherentException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements PaiementService {
    private final PaiementRepository paiementRepository;
    private final AdherentRepository adherentRepository;
    @Override
    public PaiementCotisation addPaiement(PaiementRequest request) {

        Adherent adherent = adherentRepository.findById(request.getAdherentId())
                .orElseThrow(() -> new AdherentException("Adherent not found"));



        // Création du paiement
        PaiementCotisation paiement = new PaiementCotisation();
        paiement.setMontant(request.getMontant());

        String reference = request.getReference();
        if (paiementRepository.existsByReference(reference)) {
            throw new  AdherentException("Reference déjà utilisée");
        }
        paiement.setReference(reference);
        paiement.setPhotoPaiement(request.getPhotoPaiement());
        paiement.setPhotos(request.getPhotos());
        paiement.setModePaiement(request.getModePaiement());
        paiement.setAdherent(adherent);


        return paiementRepository.save(paiement);
    }

    @Override
    public List<PaiementCotisation> getAllPaiementsByAdherentId(Long adherentId) {
        return paiementRepository.findAllByAdherentId(adherentId);
    }
}
