package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.AgentCreateRequest;
import com.example.sen_scu.dto.sen_csu.AgentUpdateRequest;
import com.example.sen_scu.dto.sen_csu.ChangePasswordRequest;
import com.example.sen_scu.model.sen_csu.Agent;

import java.util.List;
import java.util.Optional;

public interface AgentService {

    Agent create(AgentCreateRequest request);

    List<Agent> getAll();

    Optional<Agent> findById(String id);

    Optional<Agent> findByEmail(String email);

    Agent update(String id, AgentUpdateRequest request);

    void delete(String id);

    void changePassword(String id, ChangePasswordRequest request);

    void resetPassword(String id, String newPassword);

    long count();
}
