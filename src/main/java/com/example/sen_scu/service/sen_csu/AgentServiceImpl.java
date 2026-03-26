package com.example.sen_scu.service.sen_csu;


import com.example.sen_scu.model.sen_csu.Agent;
import com.example.sen_scu.repository.sen_csu.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Agent add(Agent agent) {
        agent.setPassword(passwordEncoder.encode("123"));

        return agentRepository.save(agent);
    }


    @Override
    public List<Agent> getAll() {
        return agentRepository.findAll();
    }

    @Override
    public boolean isLogin(String email, String password) {
        Agent agent = agentRepository.findByEmail(email).orElse(null);
        if (agent == null) return false;
        return passwordEncoder.matches(password, agent.getPassword());
    }

    @Override
    public Agent login(String email, String password) {
        Agent agent = agentRepository.findByEmail(email).orElse(null);
        if (agent == null) return null;

        if (passwordEncoder.matches(password, agent.getPassword())) {
            return agent;
        }
        return null;
    }
}
