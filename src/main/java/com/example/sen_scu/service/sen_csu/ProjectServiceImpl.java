package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.ProjetWithAdherentRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.Projet;
import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import com.example.sen_scu.repository.sen_csu.AgentRepository;
import com.example.sen_scu.repository.sen_csu.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final AdherentRepository adherentRepository;
    private final AgentRepository agentRepository;

    @Override
    public Projet createProjetWithAdherent(ProjetWithAdherentRequest request) {

        // 1️⃣ Création de l'adhérent
        Adherent adh = new Adherent();
        adh.setPrenoms(request.getPrenoms());
        adh.setNom(request.getNom());
        adh.setSexe(request.getSexe());
        adh.setRegime(request.getRegime());
        adh.setRegion(request.getRegion());
        adh.setDepartement(request.getDepartement());
        adh.setCommune(request.getCommune());
        adh.setTypeBenef(request.getTypeBenef());
        adh.setTypeAdhesion(request.getTypeAdhesion());
        adh.setDateNaissance(request.getDateNaissance());
        adh.setWhatsapp(request.getWhatsapp());
        adh.setLieuNaissance(request.getLieuNaissance());
        adh.setAdresse(request.getAdresse());
        adh.setLienParent(request.getLienParent());
        adh.setPhoto(request.getPhoto());
        adh.setPhotoRecto(request.getPhotoRecto());
        adh.setPhotoVerso(request.getPhotoVerso());
        adh.setSituationM(request.getSituationM());
        adh.setNumeroCNi(request.getNumeroCNi());
        adh.setSecteurActivite(request.getSecteurActivite());
        adh.setCreatedAt(LocalDateTime.now());

        // 🔥 Ajout de l'agent si un ID est envoyé
        if (request.getAgentId() != null) {
            adh.setAgent(
                    agentRepository.findById(request.getAgentId())
                            .orElseThrow(() -> new RuntimeException("Agent introuvable"))
            );
        }

        // 👉 On sauvegarde l'adhérent
        adherentRepository.save(adh);


        // 2️⃣ Création du projet
        Projet projet = new Projet();
        projet.setNomProjet(request.getNomProjet());
        projet.setDescription(request.getDescription());
        projet.setAdherent(adh);  // association adhérent → projet

        // 👉 On sauvegarde le projet
        return projectRepository.save(projet);
    }

    @Override
    public List<Projet> getAllProjects() {
        return projectRepository.findAll();
    }
}
