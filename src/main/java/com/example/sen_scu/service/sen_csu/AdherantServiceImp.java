package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.AdherentRequest;
import com.example.sen_scu.mapper.AdherentMapper;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.Agent;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import com.example.sen_scu.repository.sen_csu.AgentRepository;
import com.example.sen_scu.repository.sen_csu.PaiementRepository;
import com.example.sen_scu.repository.sen_csu.ProjectRepository;
import com.example.sen_scu.service.sen_csu.exception.AdherentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdherantServiceImp implements AdherentService {

    // Constantes pour éviter les "Magic Numbers"
    private static final double FRAIS_DOSSIER = 1000.0;
    private static final double COTISATION_PAR_TETE = 3500.0;
    private static final String DEFAULT_PASSWORD = "acmu00";


    private final AdherentRepository adherentRepository;
    private final AgentRepository agentRepository;
    private final AdherentMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final PaiementRepository paiementRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "adherents", allEntries = true),
            @CacheEvict(value = "adherentsByAgent", allEntries = true),
            @CacheEvict(value = "dashboardStats", allEntries = true)
    })
    public Adherent saveWithDependants(AdherentRequest request, String agentId) {
        log.info("📥 Réception nouvelle demande d'adhésion : {}", request.getNumeroCNi());

        // 1. Validation de base
        validateRequest(request);

        // 2. Récupération de l'agent
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new AdherentException("Agent " + agentId + " non trouvé"));

        // 3. Mapping DTO -> Entity (Utilisation du Mapper injecté)
        Adherent adherent = mapper.toEntity(request);

        // 4. Sécurisation et Données par défaut
        adherent.setAgent(agent);
        adherent.setRole("Adherant");
        adherent.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));

        // 5. Gestion des Personnes à Charge & Calcul du Montant
        processDependantsAndPricing(adherent, request);

        // 6. Persistance
        Adherent saved = adherentRepository.save(adherent);
        log.info("✅ Adhérent créé avec succès : ID {}", saved.getId());
        return saved;
    }

    private void validateRequest(AdherentRequest request) {
        if (request == null)
            throw new AdherentException("La requête est vide.");

        String cni = request.getNumeroCNi();
        if (!StringUtils.hasText(cni)) {
            throw new AdherentException("Le numéro CNI est requis.");
        }

        if (adherentRepository.existsByNumeroCNi(cni)) {
            throw new AdherentException("Un adhérent avec ce numéro CNI existe déjà.");
        }
    }

    private void processDependantsAndPricing(Adherent adherent, AdherentRequest request) {
        List<PersonneCharge> dependants = request.getPersonnesCharge();
        int nbDependants = (dependants != null) ? dependants.size() : 0;

        if (dependants != null) {
            dependants.forEach(pc -> pc.setAdherent(adherent));
            adherent.setPersonnesCharge(dependants);
        }

        // Calcul : (Adhérent + Personnes à charge) * Cotisation + Frais
        double total = (1 + nbDependants) * COTISATION_PAR_TETE + FRAIS_DOSSIER;
        adherent.setMontantTotal(total);
    }

    @Override
    @Cacheable(value = "adherents")
    public List<Adherent> getAllAdherents() {
        return adherentRepository.findAll();
    }

    @Override
    public Optional<Adherent> loginAdherent(String whatsapp, String password) {
        return Optional.ofNullable(adherentRepository.findAdherentByWhatsapp(whatsapp))
                .filter(adherent -> passwordEncoder.matches(password, adherent.getPassword()));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "adherents", allEntries = true),
            @CacheEvict(value = "adherentsByAgent", allEntries = true),
            @CacheEvict(value = "adherentById", key = "#id"),
            @CacheEvict(value = "dashboardStats", allEntries = true)
    })
    public void deleteAdherent(String id) {
        if (!adherentRepository.existsById(id)) {
            throw new AdherentException("Impossible de supprimer : Adhérent introuvable.");
        }

        paiementRepository.deleteByAdherentId(id);
        projectRepository.deleteByAdherentId(id);
        adherentRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "adherentsByAgent", key = "#agentId")
    public List<Adherent> getAllAdherentsByAgentId(String agentId) {
        return adherentRepository.findAllByAgentId(agentId);
    }

    @Override
    @Cacheable(value = "adherentById", key = "#id")
    public Optional<Adherent> getAdherentById(String id) {
        return adherentRepository.findById(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "adherents", allEntries = true),
            @CacheEvict(value = "adherentsByAgent", allEntries = true),
            @CacheEvict(value = "adherentById", key = "#adherentId"),
            @CacheEvict(value = "dashboardStats", allEntries = true)
    })
    public Adherent updateAdherent(String adherentId, AdherentRequest adherent) {
        return adherentRepository.findById(adherentId)
                .map(existing -> {
                    existing.setNom(adherent.getNom());
                    existing.setPrenoms(adherent.getPrenoms());
                    existing.setAdresse(adherent.getAdresse());
                    existing.setLieuNaissance(adherent.getLieuNaissance());
                    existing.setSexe(adherent.getSexe());

                    // ✅ Date formatée
                    existing.setDateNaissance(adherent.getDateNaissance());

                    existing.setSituationMatrimoniale(adherent.getSituationMatrimoniale());
                    existing.setWhatsapp(adherent.getWhatsapp());
                    existing.setSecteurActivite(adherent.getSecteurActivite());
                    existing.setRegion(adherent.getRegion());
                    existing.setDepartement(adherent.getDepartement());
                    existing.setCommune(adherent.getCommune());
                    existing.setMontantTotal(adherent.getMontantTotal());
                    existing.setPhoto(adherent.getPhoto());
                    existing.setPhotoRecto(adherent.getPhotoRecto());
                    existing.setPhotoVerso(adherent.getPhotoVerso());

                    // ✅ Champs obligatoires du DTO front
                    existing.setTypePiece(adherent.getTypePiece());
                    existing.setNumeroCNi(adherent.getNumeroCNi());

                    // ✅ Synchronisation des personnes à charge
                    existing.getPersonnesCharge().clear();
                    if (adherent.getPersonnesCharge() != null) {
                        adherent.getPersonnesCharge().forEach(pc -> pc.setAdherent(existing));
                        existing.getPersonnesCharge().addAll(adherent.getPersonnesCharge());
                    }

                    return adherentRepository.save(existing);
                })
                .orElseThrow(() -> new AdherentException("Adhérent " + adherentId + " non trouvé."));
    }

    @Override
    public List<Adherent> getAllAdherentsByDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return adherentRepository.findAllByCreatedAtBetween(start, end);
    }

    public void syncAdherent(AdherentRequest request) {
        if (adherentRepository.existsByClientUUID(request.getClientUUID())) {
            return;
        }
        adherentRepository.save(mapper.toEntity(request));
    }
}