package com.example.sen_scu.repository.sen_csu;


import com.example.sen_scu.model.sen_csu.Agent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AgentRepository extends MongoRepository<Agent, String> {

    Optional<Agent> findByEmail(String email);

    boolean existsByEmail(String email);
}
