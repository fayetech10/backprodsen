package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.AdherentRequest;
import com.example.sen_scu.dto.sen_csu.PersonneChargeRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.Agent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdherentService {

    Adherent saveWithDependants(AdherentRequest request, Long AgentId);

    List<Adherent> getAllAdherents();

    Optional<Adherent> loginAdherent(String whatsapp, String password);

    void deleteAdherent(Long id);

    List<Adherent> getAllAdherentsByAgentId(Long agentId);

    void syncAdherent(AdherentRequest request);

    Optional<Adherent> getAdherentById(Long id);

    Adherent updateAdherent(Long adherentId, AdherentRequest request);

    java.util.List<Adherent> getAllAdherentsByDateRange(LocalDateTime start, LocalDateTime end);

}
