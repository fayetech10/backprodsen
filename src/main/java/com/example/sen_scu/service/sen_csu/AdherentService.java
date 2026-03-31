package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.AdherentRequest;
import com.example.sen_scu.model.sen_csu.Adherent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdherentService {

    Adherent saveWithDependants(AdherentRequest request, String AgentId);

    List<Adherent> getAllAdherents();

    Optional<Adherent> loginAdherent(String whatsapp, String password);

    void deleteAdherent(String id);

    List<Adherent> getAllAdherentsByAgentId(String agentId);

    void syncAdherent(AdherentRequest request);

    Optional<Adherent> getAdherentById(String id);

    Adherent updateAdherent(String adherentId, AdherentRequest request);

    java.util.List<Adherent> getAllAdherentsByDateRange(LocalDateTime start, LocalDateTime end);

}
