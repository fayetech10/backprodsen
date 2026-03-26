package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.PersonneChargeRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import com.example.sen_scu.repository.sen_csu.AgentRepository;
import com.example.sen_scu.repository.sen_csu.PersonneChargeRepository;
import com.example.sen_scu.service.sen_csu.exception.AdherentException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PersonneEnchargeSeviceImpl  implements PersonneEnchargeSevice {
    private final PersonneChargeRepository personneChargeRepository;
    private final AdherentRepository adherentRepository;
    private final AgentRepository agentRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public PersonneCharge savePersonneChargeRequest(PersonneChargeRequest request, Long adherentId) {
// Récupérer l'adhérent
        Adherent adherent = adherentRepository.findById(adherentId)
                .orElseThrow(() -> new AdherentException("Adhérent non trouvé"));

        // Création de la personne en charge
        PersonneCharge personne = new PersonneCharge();
        personne.setPrenoms(request.getPrenoms());
        personne.setNom(request.getNom());
        personne.setSexe(request.getSexe());
        personne.setDateNaissance(request.getDateNaissance());
        personne.setLieuNaissance(request.getLieuNaissance());
        personne.setAdresse(request.getAdresse());
        personne.setWhatsapp(request.getTelephone());
        personne.setLienParent(request.getLienParent());
        personne.setPhoto(request.getPhoto());
        personne.setPhotoRecto(request.getPhotoRecto());
        personne.setPhotoVerso(request.getPhotoVerso());
        personne.setSituationM(request.getSituationM());
        personne.setNumeroCNi(request.getNumeroCNi());
        personne.setNumeroExtrait(request.getNumeroExtrait());

        // Associer les clés étrangères
        personne.setAdherent(adherent);

        if (adherent.getMontantTotal() == null) {
            adherent.setMontantTotal(0.0);
        }
        adherent.setMontantTotal(adherent.getMontantTotal() + 3500);



        // Sauvegarder en base
        return personneChargeRepository.save(personne);

    }

    @Override
    public PersonneCharge update(Long adherentId,Long pcId, PersonneChargeRequest request) {

        PersonneCharge existing = personneChargeRepository
                .findByIdAndAdherentId(pcId, adherentId)
                .orElseThrow(() -> new RuntimeException(
                        "Personne en charge " + pcId + " n'appartient pas à l'adhérent " + adherentId));
        existing.setPrenoms(request.getPrenoms());
        existing.setNom(request.getNom());
        existing.setSexe(request.getSexe());
        existing.setDateNaissance(request.getDateNaissance());
        existing.setLieuNaissance(request.getLieuNaissance());
        existing.setAdresse(request.getAdresse());
        existing.setWhatsapp(request.getTelephone());
        existing.setLienParent(request.getLienParent());
        existing.setSituationM(request.getSituationM());
        existing.setNumeroExtrait(request.getNumeroExtrait());
        existing.setPhoto(request.getPhoto());
        existing.setPhotoRecto(request.getPhotoRecto());
        existing.setPhotoVerso(request.getPhotoVerso());
        return personneChargeRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long adherentId, Long pcId) {
        PersonneCharge pc = personneChargeRepository.findByIdAndAdherentId(pcId, adherentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Personne en charge " + pcId + " n'appartient pas à l'adhérent " + adherentId));
        Adherent adherent = pc.getAdherent();
        personneChargeRepository.delete(pc);
        adherent.setMontantTotal(adherent.getMontantTotal() - 3500);
        adherentRepository.save(adherent);
    }

}
