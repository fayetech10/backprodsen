package com.example.sen_scu.dto.sen_csu;

import com.example.sen_scu.model.sen_csu.Agent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentResponse {
    private String id;
    private String name;
    private String prenom;
    private String email;
    private String telephone;
    private String role;
    private String photo;
    private String sexe;
    private String dateNaissance;
    private String adresse;
    private String region;
    private String departement;
    private String commune;
    private boolean active;

    public static AgentResponse fromEntity(Agent agent) {
        return AgentResponse.builder()
                .id(agent.getId())
                .name(agent.getName())
                .prenom(agent.getPrenom())
                .email(agent.getEmail())
                .telephone(agent.getTelephone())
                .role(agent.getRole())
                .photo(agent.getPhoto())
                .sexe(agent.getSexe())
                .dateNaissance(agent.getDateNaissance())
                .adresse(agent.getAdresse())
                .region(agent.getRegion())
                .departement(agent.getDepartement())
                .commune(agent.getCommune())
                .active(agent.isActive())
                .build();
    }
}
