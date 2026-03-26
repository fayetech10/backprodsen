package com.example.sen_scu.mapper;

import com.example.sen_scu.dto.sen_csu.AdherentRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import org.springframework.stereotype.Component;

@Component
public class AdherentMapper {

    public Adherent toEntity(AdherentRequest request) {
        Adherent adherent = new Adherent();

        adherent.setNom(request.getNom());
        adherent.setPrenoms(request.getPrenoms());
        adherent.setAdresse(request.getAdresse());
        adherent.setLieuNaissance(request.getLieuNaissance());
        adherent.setSexe(request.getSexe());
        adherent.setDateNaissance(request.getDateNaissance());
        adherent.setSituationM(request.getSituationMatrimoniale());
        adherent.setWhatsapp(request.getWhatsapp());
        adherent.setSecteurActivite(request.getSecteurActivite());
        adherent.setNumeroCNi(request.getNumeroCNi());
        adherent.setDepartement(request.getDepartement());
        adherent.setCommune(request.getCommune());
        adherent.setRegion(request.getRegion());
        adherent.setClientUUID(request.getClientUUID());
        adherent.setPhoto(request.getPhoto());
        adherent.setPhotoRecto(request.getPhotoRecto());
//        adherent.getPhotoVerso(request.getPhotoVerso());

        // TODO : Mapper les personnes à charge si besoin
//         adherent.setPersonnesCharge(mapDependants(request.getPersonnesCharge()));

        return adherent;
    }

}
