package com.example.sen_scu.repository.sen_csu;


import com.example.sen_scu.model.sen_csu.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    Optional<Agent> findByEmail(String email);

    boolean existsByEmail(String email);
}

