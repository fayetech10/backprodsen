package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.AgentCreateRequest;
import com.example.sen_scu.dto.sen_csu.AgentUpdateRequest;
import com.example.sen_scu.dto.sen_csu.ChangePasswordRequest;
import com.example.sen_scu.model.sen_csu.Agent;
import com.example.sen_scu.repository.sen_csu.AgentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Agent create(AgentCreateRequest request) {
        // Vérifier si l'email existe déjà
        if (agentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un agent avec cet email existe déjà: " + request.getEmail());
        }

        String rawPassword = (request.getPassword() != null && !request.getPassword().isBlank())
                ? request.getPassword()
                : "123456"; // mot de passe par défaut

        Agent agent = Agent.builder()
                .name(request.getName())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .password(passwordEncoder.encode(rawPassword))
                .telephone(request.getTelephone())
                .photo(request.getPhoto())
                .dateNaissance(request.getDateNaissance())
                .sexe(request.getSexe())
                .departement(request.getDepartement())
                .commune(request.getCommune())
                .region(request.getRegion())
                .adresse(request.getAdresse())
                .role(request.getRole() != null ? request.getRole() : "AGENT")
                .build();

        Agent saved = agentRepository.save(agent);
        log.info("Agent créé avec succès: {} {}", saved.getPrenom(), saved.getName());
        return saved;
    }

    @Override
    public List<Agent> getAll() {
        return agentRepository.findAll();
    }

    @Override
    public Optional<Agent> findById(String id) {
        return agentRepository.findById(id);
    }

    @Override
    public Optional<Agent> findByEmail(String email) {
        return agentRepository.findByEmail(email);
    }

    @Override
    public Agent update(String id, AgentUpdateRequest request) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé avec l'ID: " + id));

        // Mise à jour sélective — ne modifie que les champs non-null
        if (request.getName() != null) agent.setName(request.getName());
        if (request.getPrenom() != null) agent.setPrenom(request.getPrenom());
        if (request.getEmail() != null) {
            // Vérifier unicité du nouvel email
            if (!request.getEmail().equals(agent.getEmail()) && agentRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Cet email est déjà utilisé par un autre agent");
            }
            agent.setEmail(request.getEmail());
        }
        if (request.getTelephone() != null) agent.setTelephone(request.getTelephone());
        if (request.getPhoto() != null) agent.setPhoto(request.getPhoto());
        if (request.getDateNaissance() != null) agent.setDateNaissance(request.getDateNaissance());
        if (request.getSexe() != null) agent.setSexe(request.getSexe());
        if (request.getDepartement() != null) agent.setDepartement(request.getDepartement());
        if (request.getCommune() != null) agent.setCommune(request.getCommune());
        if (request.getRegion() != null) agent.setRegion(request.getRegion());
        if (request.getAdresse() != null) agent.setAdresse(request.getAdresse());
        if (request.getRole() != null) agent.setRole(request.getRole());

        Agent updated = agentRepository.save(agent);
        log.info("Agent mis à jour: {}", updated.getId());
        return updated;
    }

    @Override
    public void delete(String id) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé avec l'ID: " + id));
        agentRepository.delete(agent);
        log.info("Agent supprimé: {}", id);
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest request) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé avec l'ID: " + id));

        if (!passwordEncoder.matches(request.getOldPassword(), agent.getPassword())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect");
        }

        agent.setPassword(passwordEncoder.encode(request.getNewPassword()));
        agentRepository.save(agent);
        log.info("Mot de passe changé pour l'agent: {}", id);
    }

    @Override
    public void resetPassword(String id, String newPassword) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé avec l'ID: " + id));

        String password = (newPassword != null && !newPassword.isBlank()) ? newPassword : "123456";
        agent.setPassword(passwordEncoder.encode(password));
        agentRepository.save(agent);
        log.info("Mot de passe réinitialisé pour l'agent: {}", id);
    }

    @Override
    public long count() {
        return agentRepository.count();
    }
}
