package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.PaiementRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.PaiementCotisation;
import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import com.example.sen_scu.repository.sen_csu.PaiementRepository;
import com.example.sen_scu.service.sen_csu.exception.AdherentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {
    private final PaiementRepository paiementRepository;
    private final AdherentRepository adherentRepository;
    private static final Set<String> ALLOWED_PAYMENT_MODES = Set.of(
            "Virement", "Wave", "Orange Money", "Espèces", "Chèque"
    );

    @Override
    public PaiementCotisation addPaiement(PaiementRequest request) {
        validateRequest(request);

        Adherent adherent = adherentRepository.findById(request.getAdherentId())
                .orElseThrow(() -> new AdherentException("Adherent not found"));

        // Création du paiement
        PaiementCotisation paiement = new PaiementCotisation();
        paiement.setMontant(request.getMontant());

        String reference = request.getReference().trim();
        if (paiementRepository.existsByReference(reference)) {
            throw new AdherentException("Reference déjà utilisée");
        }
        paiement.setReference(reference);
        paiement.setPhotoPaiement(request.getPhotoPaiement());
        paiement.setPhotos(request.getPhotos());
        paiement.setModePaiement(request.getModePaiement());
        paiement.setAdherent(adherent);

        return paiementRepository.save(paiement);
    }

    @Override
    public List<PaiementCotisation> getAllPaiementsByAdherentId(String adherentId) {
        return paiementRepository.findAllByAdherentId(adherentId);
    }

    private void validateRequest(PaiementRequest request) {
        if (request == null) {
            throw new AdherentException("Requête de paiement invalide");
        }
        if (request.getAdherentId() == null || request.getAdherentId().isBlank()) {
            throw new AdherentException("AdherentId est obligatoire");
        }
        if (request.getMontant() == null || request.getMontant() <= 0) {
            throw new AdherentException("Montant invalide");
        }
        if (request.getReference() == null || request.getReference().isBlank()) {
            throw new AdherentException("Référence obligatoire");
        }
        if (request.getModePaiement() == null || request.getModePaiement().isBlank()) {
            throw new AdherentException("Mode de paiement obligatoire");
        }
        if (!ALLOWED_PAYMENT_MODES.contains(request.getModePaiement())) {
            throw new AdherentException("Mode de paiement non supporté");
        }

        if (isMobileMoneyMode(request.getModePaiement())) {
            String cleanedReference = request.getReference().trim().replaceAll("\\s+", "");
            boolean hasDigit = cleanedReference.chars().anyMatch(Character::isDigit);
            if (cleanedReference.length() < 8 || !hasDigit) {
                throw new AdherentException("Référence mobile money invalide");
            }
        }
    }

    private boolean isMobileMoneyMode(String modePaiement) {
        return "Wave".equals(modePaiement) || "Orange Money".equals(modePaiement);
    }
}
