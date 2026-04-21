package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.PersonneChargeRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import com.example.sen_scu.repository.sen_csu.PersonneChargeRepository;
import com.example.sen_scu.service.sen_csu.exception.AdherentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonneEnchargeSeviceImpl implements PersonneEnchargeSevice {
    private final PersonneChargeRepository personneChargeRepository;
    private final AdherentRepository adherentRepository;

    @Override
    public PersonneCharge savePersonneChargeRequest(PersonneChargeRequest request, String adherentId) {
        // Récupérer l'adhérent (par ID technique ou par UUID client)
        Adherent adherent = adherentRepository.findById(adherentId)
                .or(() -> adherentRepository.findByClientUUID(adherentId))
                .orElseThrow(() -> new AdherentException("Adhérent non trouvé avec l'identifiant : " + adherentId));

        // Création de la personne en charge
        PersonneCharge personne = new PersonneCharge();
        personne.setPrenoms(request.getPrenoms());
        personne.setNom(request.getNom());
        personne.setSexe(request.getSexe());
        personne.setDateNaissance(request.getDateNaissance());
        personne.setLieuNaissance(request.getLieuNaissance());
        personne.setAdresse(request.getAdresse());
        personne.setWhatsapp(request.getTelephone()); // On garde telephone -> whatsapp
        personne.setLienParent(request.getLienParent());
        personne.setPhoto(request.getPhoto());
        personne.setPhotoRecto(request.getPhotoRecto());
        personne.setPhotoVerso(request.getPhotoVerso());
        personne.setSituationM(request.getSituationM());
        personne.setNumeroCNi(request.getNumeroCNi());
        personne.setNumeroExtrait(request.getNumeroExtrait());

        // Associer les clés étrangères
        personne.setAdherent(adherent);

        // Mise à jour de l'adhérent
        if (adherent.getMontantTotal() == null) {
            adherent.setMontantTotal(0.0);
        }
        adherent.setMontantTotal(adherent.getMontantTotal() + 3500);
        
        // Ajouter à la liste de l'adhérent pour la cohérence de l'objet
        if (adherent.getPersonnesCharge() == null) {
            adherent.setPersonnesCharge(new java.util.ArrayList<>());
        }
        adherent.getPersonnesCharge().add(personne);

        // Sauvegarder la personne en charge
        PersonneCharge saved = personneChargeRepository.save(personne);
        
        // Sauvegarder l'adhérent pour mettre à jour son montant total et sa liste
        adherentRepository.save(adherent);
        
        return saved;
    }

    @Override
    public PersonneCharge update(String adherentId, String pcId, PersonneChargeRequest request) {

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
    public void delete(String adherentId, String pcId) {
        PersonneCharge pc = personneChargeRepository.findByIdAndAdherentId(pcId, adherentId)
                .orElseThrow(() -> new RuntimeException(
                        "Personne en charge " + pcId + " n'appartient pas à l'adhérent " + adherentId));
        Adherent adherent = pc.getAdherent();
        personneChargeRepository.delete(pc);
        adherent.setMontantTotal(adherent.getMontantTotal() - 3500);
        adherentRepository.save(adherent);
    }

}
