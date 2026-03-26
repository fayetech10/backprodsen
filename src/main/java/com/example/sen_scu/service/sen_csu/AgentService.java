package com.example.sen_scu.service.sen_csu;


import com.example.sen_scu.model.sen_csu.Agent;

import java.util.List;

public interface AgentService {
    Agent add (Agent agent);
    List<Agent> getAll ();
    boolean isLogin (String email, String password);
    Agent login (String email, String password);
}
