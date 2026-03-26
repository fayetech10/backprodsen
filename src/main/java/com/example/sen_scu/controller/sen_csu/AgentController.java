package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.dto.sen_csu.AdherentRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.Agent;
import com.example.sen_scu.service.sen_csu.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/agent")
public class AgentController {
private final AgentService agentService;

@Operation
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Agent agent) {
        try {
            Agent saved = agentService.add(agent);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Agent créé avec succès",
                    "data", Map.of("adherent_id", saved)
            ));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Le numéro CNI existe déjà."
            ));
        }
    }
    @GetMapping("/all")
        public ResponseEntity<?> getAll() {
        List<Agent> agents = agentService.getAll();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Liste des adhérents récupérée avec succès",
                "data", agents
        ));}
    // Login
    @Operation
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {

        String email = credentials.get("email");
        String password = credentials.get("password");

        Agent agent = agentService.login(email, password);

        if (agent != null) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Connexion réussie",
                    "data", agent
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Email ou mot de passe incorrect"
            ));
        }
    }
}
